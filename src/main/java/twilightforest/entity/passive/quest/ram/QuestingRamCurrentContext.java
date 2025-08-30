package twilightforest.entity.passive.quest.ram;

import tamaized.beanification.Component;

@Component
public class QuestingRamCurrentContext {

	private QuestingRamContext context = QuestingRamContext.FALLBACK;

	public void setContext(QuestingRamContext context) {
		this.context = context;
	}

	public QuestingRamContext getContext() {
		return context;
	}

}
