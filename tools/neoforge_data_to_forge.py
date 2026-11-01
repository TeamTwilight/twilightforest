#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Convertit les donnees et ressources du mod ecrites pour NeoForge vers leurs equivalents Forge 1.21.1.

Seuls les renommages dont l'equivalent Forge a ete verifie dans le jar de Forge 52.1.0 sont faits :

  conditions de butin     neoforge:can_item_perform_ability {ability}
                       -> forge:can_tool_perform_action   {action}
  conditions de chargement
                          "neoforge:conditions": [c1, c2...]   (liste, ET implicite)
                       -> "forge:condition": c1                 (un seul objet)
                          ou {"type": "forge:and", "values": [c1, c2...]}
                          types neoforge:{mod_loaded,not,and,or,true,false,item_exists,tag_empty}
                       -> forge:*  (memes noms de champs : modid, value, values, item, tag)
  ingredient              neoforge:difference {base, subtracted} -> forge:difference (memes champs)
                          neoforge:components {items, components, strict}
                       -> twilightforest:components (meme format, type ajoute par le mod :
                          Forge n'a que partial_nbt et nbt, qui ne lisent que custom_data)
  liste d'ingredients      [ing1, ing2...] qui designe UN ingredient (valeur d'une lettre de
                          "key", ou element d'une liste d'ingredients) et contient un ingredient
                          a type : NeoForge l'accepte, Forge veut
                       -> {"type": "forge:compound", "children": [ing1, ing2...]}
  modeles (client)        loader neoforge:{item_layers,composite,separate_transforms} -> forge:*
                          parent neoforge:item/default -> forge:item/default

Usage : python tools/neoforge_data_to_forge.py [--dry-run]
"""

import json
import os
import sys
from collections import Counter

ROOTS = ["src/main/resources", "src/generated/resources"]

CONDITION_TYPES = {"mod_loaded", "not", "and", "or", "true", "false", "item_exists", "tag_empty"}
MODEL_LOADERS = {"item_layers", "composite", "separate_transforms"}

stats = Counter()
# Vrai pendant le traitement d'une recette : l'enveloppe forge:compound n'a de sens que la.
in_recipe = False


def convert_condition(cond):
    """Une condition de chargement NeoForge -> Forge (recursive pour and/or/not)."""
    if isinstance(cond, dict):
        t = cond.get("type")
        if isinstance(t, str) and t.startswith("neoforge:") and t[len("neoforge:"):] in CONDITION_TYPES:
            cond = dict(cond)
            cond["type"] = "forge:" + t[len("neoforge:"):]
            stats["condition type"] += 1
        if "value" in cond:
            cond["value"] = convert_condition(cond["value"])
        if "values" in cond and isinstance(cond["values"], list):
            cond["values"] = [convert_condition(c) for c in cond["values"]]
    return cond


def is_custom_list(node):
    """Une liste d'ingredients dont l'un a un type (hors ingredient vanilla item/tag)."""
    return (in_recipe and isinstance(node, list) and node
            and all(isinstance(e, dict) for e in node)
            and any("type" in e for e in node))


def compound(node):
    stats["ingredient compound"] += 1
    return {"type": "forge:compound", "children": node}


def walk(node):
    if isinstance(node, list):
        return [compound(x) if is_custom_list(x) else x for x in (walk(x) for x in node)]
    if not isinstance(node, dict):
        return node

    out = {}
    for key, value in node.items():
        if key == "neoforge:conditions" and isinstance(value, list):
            conds = [convert_condition(walk(c)) for c in value]
            out["forge:condition"] = conds[0] if len(conds) == 1 else {"type": "forge:and", "values": conds}
            stats["neoforge:conditions -> forge:condition"] += 1
            continue
        out[key] = walk(value)

    # lettres d'une recette avec motif : chaque valeur est un seul ingredient
    if isinstance(out.get("key"), dict) and "pattern" in out:
        out["key"] = {k: compound(v) if is_custom_list(v) else v for k, v in out["key"].items()}

    # condition de butin
    if out.get("condition") == "neoforge:can_item_perform_ability":
        out = {("action" if k == "ability" else k): v for k, v in out.items()}
        out["condition"] = "forge:can_tool_perform_action"
        stats["loot can_tool_perform_action"] += 1

    # ingredient difference
    if out.get("type") == "neoforge:difference":
        out["type"] = "forge:difference"
        stats["ingredient difference"] += 1
    if out.get("type") == "neoforge:components":
        out["type"] = "twilightforest:components"
        stats["ingredient components"] += 1

    # chargeurs de modeles
    loader = out.get("loader")
    if isinstance(loader, str) and loader.startswith("neoforge:") and loader[len("neoforge:"):] in MODEL_LOADERS:
        out["loader"] = "forge:" + loader[len("neoforge:"):]
        stats["model loader"] += 1
    if out.get("parent") == "neoforge:item/default":
        out["parent"] = "forge:item/default"
        stats["model parent"] += 1

    return out


def main():
    dry = "--dry-run" in sys.argv
    changed_files = 0
    for root in ROOTS:
        for dirpath, _, files in os.walk(root):
            for name in files:
                if not name.endswith(".json"):
                    continue
                path = os.path.join(dirpath, name)
                with open(path, encoding="utf-8") as f:
                    raw = f.read()
                global in_recipe
                in_recipe = "/recipe/" in path.replace(os.sep, "/")
                if "neoforge:" not in raw and not (in_recipe and '"type"' in raw):
                    continue
                try:
                    data = json.loads(raw)
                except json.JSONDecodeError:
                    stats["json illisible"] += 1
                    continue
                new = walk(data)
                if new != data:
                    changed_files += 1
                    if not dry:
                        with open(path, "w", encoding="utf-8", newline="\n") as f:
                            json.dump(new, f, indent=2, ensure_ascii=False)
                            f.write("\n")
    print(("[simulation] " if dry else "") + f"{changed_files} fichiers modifies")
    for k, v in sorted(stats.items()):
        print(f"  {k}: {v}")


if __name__ == "__main__":
    main()
