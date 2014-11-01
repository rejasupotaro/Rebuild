package rejasupotaro.rebuild.test.models;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.robotgirl.Definition;
import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;
import rejasupotaro.robotgirl.RobotGirlConfiguration;

public class EpisodeTest extends InstrumentationTestCase {

    @Override
    public void setUp() {
        RobotGirlConfiguration conf =
                new RobotGirlConfiguration.Builder(getInstrumentation().getTargetContext())
                        .packageContext(getInstrumentation().getContext())
                        .build();
        RobotGirl.init(conf);

        Factory.define(
                new Definition(Episode.class, "episode1") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 1);
                        attrs.putString("title", "episode1");
                        return attrs;
                    }
                }, new Definition(Episode.class, "episode2") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 1);
                        attrs.putString("title", "episode1");
                        return attrs;
                    }
                }, new Definition(Episode.class, "episode3") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 3);
                        attrs.putString("title", "episode3");
                        return attrs;
                    }
                }
        );
    }

    public void testIsSameId() {
        Episode episode1 = Factory.build(Episode.class, "episode1");
        Episode episode2 = Factory.build(Episode.class, "episode2");
        Episode episode3 = Factory.build(Episode.class, "episode3");

        assertTrue(episode1.isSameEpisode(episode2));
        assertFalse(episode1.isSameEpisode(episode3));
        assertFalse(episode2.isSameEpisode(episode3));
    }
}
