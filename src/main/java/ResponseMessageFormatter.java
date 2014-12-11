import java.util.*;
import microsoft.exchange.webservices.data.*;

public class ResponseMessageFormatter {
    Properties prop;
    public ResponseMessageFormatter(Properties prop) {
        this.prop = prop;
    }

    /**
     * IRCメッセージ用に予定リストを整形する
     * @param calendarEvents 予定リスト
     * @param fromMillis 終了日時がこの日時より前の予定は除く
     */
    public String format(Collection<CalendarEvent> calendarEvents, long fromMillis) {
        if (calendarEvents == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        Calendar cal = Calendar.getInstance();
        int prevDate = cal.get(Calendar.DATE);
        String headmark = "▼";
        for (CalendarEvent a : calendarEvents) {
            Date start = a.getStartTime();
            Date end = a.getEndTime();
            if (end.getTime() < fromMillis) {
                continue;
            }
            cal.setTime(start);
            int date = cal.get(Calendar.DATE);
            if (date != prevDate) {
                headmark = "●";
                fmt.format("%s%td日", headmark, start);
                prevDate = date;
            } else {
                sb.append(headmark);
            }
            fmt.format("%tR", start);
            sb.append("-");
            cal.setTime(end);
            date = cal.get(Calendar.DATE);
            if (date != prevDate) {
                headmark = "●";
                fmt.format("%s%td日", headmark, end);
                prevDate = date;
            }
            fmt.format("%tR", end);

            String subj = null;
            String loc = null;
            CalendarEventDetails details = a.getDetails();
            if (details != null) {
                subj = details.getSubject();
                loc = details.getLocation();
            }
            if (subj == null) {
                subj = "-";
            }
            sb.append(" ");
            sb.append(subj);
            if (loc != null) {
                fmt.format("(%s)", shortenLocation(loc));
            }
        }
        return sb.toString();
    }

    String shortenLocation(String location) {
        // TODO
        return location;
    }
}
