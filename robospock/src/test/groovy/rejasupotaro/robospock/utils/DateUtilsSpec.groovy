package rejasupotaro.robospock.utils

import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.utils.DateUtils

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class DateUtilsSpec extends RoboSpecification {

    def "millis to duration"() {
        expect:
        DateUtils.durationToInt(data) == result

        where:
        data      | result
        "00:10"   | 10000
        "01:30"   | 90000
        "51:34"   | 3094000
        "1:02:13" | 3733000
    }

    def "duration to millis"() {
        expect:
        DateUtils.formatCurrentTime(data) == result

        where:
        data      | result
        1000      | "00:01"
        10000     | "00:10"
        100000    | "01:40"
    }

    def "format pub date"() {
        expect:
        DateUtils.formatPubDate(data) == result

        where:
        data      | result
        "Thu, 31 Oct 2013 00:00:00 -0700" | "Oct 31 2013"
        "Tue, 11 Jun 2013 00:00:00 -0700" | "Jun 11 2013"
        "Wed, 13 Mar 2013 00:00:00 -0700" | "Mar 13 2013"
    }

    def "month as String"() {
        expect:
        DateUtils.monthToName(data) == result

        where:
        data | result
        1    | "Jan"
        2    | "Feb"
        3    | "Mar"
        4    | "Apr"
        5    | "May"
        6    | "Jun"
        7    | "Jul"
        8    | "Aug"
        9    | "Sep"
        10   | "Oct"
        11   | "Nov"
        12   | "Dec"
        13   | ""
    }
}
