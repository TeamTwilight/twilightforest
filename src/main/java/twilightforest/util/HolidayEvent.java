package twilightforest.util;

import twilightforest.beans.Component;

import java.time.Month;
import java.time.MonthDay;

@Component
public class HolidayEvent {

	public boolean isHalloweenWeek() {
		MonthDay now = MonthDay.now();
		return now.isAfter(MonthDay.of(Month.OCTOBER, 19)) && now.isBefore(MonthDay.of(Month.NOVEMBER, 4));
	}

}
