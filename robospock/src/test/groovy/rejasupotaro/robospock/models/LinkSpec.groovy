package rejasupotaro.robospock.models

import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.models.Link

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class LinkSpec extends RoboSpecification {

    def "get link list from source"() {
        expect:
        def links = Link.Parser.toLinkList(source)
        links.size() == size

        where:
        source | size
        null   | 0
        ""     | 0
    }
}
