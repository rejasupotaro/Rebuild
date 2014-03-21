package rejasupotaro.robospock.models

import android.os.Bundle
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import rejasupotaro.rebuild.models.Episode
import rejasupotaro.robotgirl.Definition
import rejasupotaro.robotgirl.RobotGirl
import rejasupotaro.robotgirl.RobotGirlConfiguration

@Config(manifest = "../Rebuild/src/main/AndroidManifest.xml")
class EpisodeSpec extends RoboSpecification {

    def "whether or not episode is same"() {
        setup:
        def context = Robolectric.getShadowApplication().getApplicationContext()
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(context)
                        .packageContext(context)
                        .build()
        RobotGirl.init(conf)

        when:
        rejasupotaro.robotgirl.Factory.define(
                new Definition(Episode.class, "episode1") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 1)
                        attrs.putString("title", "episode1")
                        return attrs
                    }
                }, new Definition(Episode.class, "episode2") {
            @Override
            public Bundle set(Bundle attrs) {
                attrs.putInt("episode_id", 1)
                attrs.putString("title", "episode1")
                return attrs
            }
        }, new Definition(Episode.class, "episode3") {

            @Override
            public Bundle set(Bundle attrs) {
                attrs.putInt("episode_id", 3)
                attrs.putString("title", "episode3")
                return attrs
            }
        })
        def episode1 = rejasupotaro.robotgirl.Factory.build(Episode.class, "episode1")
        def episode2 = rejasupotaro.robotgirl.Factory.build(Episode.class, "episode2")
        def episode3 = rejasupotaro.robotgirl.Factory.build(Episode.class, "episode3")

        then:
        assert episode1.isSameEpisode(episode2)
        assert !episode1.isSameEpisode(episode3)
        assert !episode2.isSameEpisode(episode3)
    }
}
