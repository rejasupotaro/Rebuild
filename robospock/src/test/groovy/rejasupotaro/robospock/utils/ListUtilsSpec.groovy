package rejasupotaro.robospock.utils

import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.utils.ListUtils

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class ListUtilsSpec extends RoboSpecification {

    def "whether or not text is empty"() {
        expect:
        ListUtils.isEmpty(data) == result

        where:
        data      | result
        null      | true
        []        | true
        ["dummy"] | false
    }

    def "max value of list"() {
        expect:
        ListUtils.max(data) == result

        where:
        data               | result
        [1, 2, 3] as int[] | 3
        [3, 2, 1] as int[] | 3
        [2, 3, 1] as int[] | 3
    }

    def "min value of list"() {
        expect:
        ListUtils.min(data) == result

        where:
        data               | result
        [1, 2, 3] as int[] | 1
        [3, 2, 1] as int[] | 1
        [2, 3, 1] as int[] | 1
    }

    def "get filtered list"() {
        expect:
        ListUtils.filterByPage(page, perPage, list).size() == result

        where:
        page | perPage | list     | result
        1    | 1      | [1]       | 1
        1    | 2      | [1, 2, 3] | 2
        1    | 3      | [1, 2, 3] | 3
        1    | 4      | [1, 2, 3] | 3
        2    | 1      | [1, 2, 3] | 1
        2    | 2      | [1, 2, 3] | 1
    }
}
