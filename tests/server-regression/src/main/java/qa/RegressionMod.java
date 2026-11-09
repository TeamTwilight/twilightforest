package qa;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Opt-in tests for disposable, loopback-only dedicated servers. */
@Mod("server_regression")
public final class RegressionMod {
  public RegressionMod() {
    NeoForge.EVENT_BUS.addListener(this::register);
  }

  private void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("serverregression")
                .requires(
                    source ->
                        source.getEntity() == null
                            && source.getServer().getLocalIp().equals("127.0.0.1"))
                .executes(
                    context -> {
                      var server = context.getSource().getServer();
                      if (!server.getPlayerList().getPlayers().isEmpty())
                        throw new IllegalStateException("Test server must have no players");
                      var level = server.overworld();
                      Checks.rows.clear();
                      try {
                        TwilightCases.run(level);
                      } catch (Exception error) {
                        throw new RuntimeException(error);
                      }
                      for (var row : Checks.rows)
                        context
                            .getSource()
                            .sendSuccess(() -> Component.literal(row.toString()), false);
                      long failed =
                          Checks.rows.stream()
                              .filter(row -> !Boolean.TRUE.equals(row.get("passed")))
                              .count();
                      if (failed != 0)
                        throw new IllegalStateException(failed + " regression checks failed");
                      return Checks.rows.size();
                    }));
  }
}
