package rejasupotaro.robospock.utils

import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.utils.NetworkUtils

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class NetworkUtilsSpec extends RoboSpecification {

    def "get package specific user agent"() {
        setup:
        def context = Robolectric.getShadowApplication().getApplicationContext();

        when:
        def userAgent = NetworkUtils.getUserAgent(context)

        then:
        assert userAgent == "rejasupotaro.rebuild/0; Android/18; unknown; ;"
    }
}
