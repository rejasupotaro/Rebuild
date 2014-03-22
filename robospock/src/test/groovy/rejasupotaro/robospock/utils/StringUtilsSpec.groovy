package rejasupotaro.robospock.utils

import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.utils.StringUtils

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class StringUtilsSpec extends RoboSpecification {

    def "capitalize text"() {
        expect:
        StringUtils.capitalize(data) == result

        where:
        data           | result
        "abc"          | "Abc"
        "hello world!" | "Hello world!"
    }

    def "remove new lines"() {
        expect:
        StringUtils.removeNewLines(data) == result

        where:
        data              | result
        "Hello World!"    | "Hello World!"
        "Hello World!\n"  |"Hello World!"
        "Hello\nWorld!\n" |"HelloWorld!"
    }

    def "build twitter link text"() {
        expect:
        StringUtils.buildTwitterLinkText(data) == result

        where:
        data                     | result
        null                     | ""
        ""                       | ""
        "Hello World"            | ""
        "Hello (World)"          | ""
        "rejasupotaro@gmail.com" | "rejasupotarogmail.com"
        "伊藤直也さん(@naoya_ito)をゲストに迎えてポッドキャスト、LTSV、RubyMotion、Perlなどについて話しました。" | "伊藤直也さん(<a href=\"https://twitter.com/naoya_ito\">@naoya_ito</a>)をゲストに迎えてポッドキャスト、LTSV、RubyMotion、Perlなどについて話しました。"
        "伊藤直也さん (@naoya_ito)、宮下剛輔さん (@gosukenator) をゲストに迎えて、Immutable Infrastructure, Docker, Packer, Serf などについて話しました。" | "伊藤直也さん (<a href=\"https://twitter.com/naoya_ito\">@naoya_ito</a>)、宮下剛輔さん (<a href=\"https://twitter.com/gosukenator\">@gosukenator</a>) をゲストに迎えて、Immutable Infrastructure, Docker, Packer, Serf などについて話しました。"
    }

    def "remove rebuild hash tag"() {
        expect:
        StringUtils.removeRebuildHashTag(data) == result

        where:
        data                             | result
        null                             | ""
        ""                               | ""
        "Hello World"                    | "Hello World"
        "パソコンサンデー待機中 #rebuildfm" | "パソコンサンデー待機中"
    }
}
