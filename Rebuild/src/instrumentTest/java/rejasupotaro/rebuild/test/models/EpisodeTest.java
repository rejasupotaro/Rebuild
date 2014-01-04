package rejasupotaro.rebuild.test.models;

import android.os.Bundle;

import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.robotgirl.ActiveAndroidTestCase;
import rejasupotaro.robotgirl.Definition;
import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;

public class EpisodeTest extends ActiveAndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Factory.define(
                new Definition(Episode.class, "episode1") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 1);
                        return attrs;
                    }
                }, new Definition(Episode.class, "episode2") {
                    @Override
                    public Bundle set(Bundle attrs) {
                        attrs.putInt("episode_id", 1);
                        return attrs;
                    }
                }, new Definition(Episode.class, "episode3") {
                    @Override
                    public Bundle set(Bundle bundle) {
                        bundle.putInt("episode_id", 3);
                        return bundle;
                    }
                }
        );
    }

    /*
    public void testIsSameId() {
        Episode episode1 = Factory.build(Episode.class, "episode1");
        Episode episode2 = Factory.build(Episode.class, "episode2");
        Episode episode3 = Factory.build(Episode.class, "episode3");

        assertTrue(episode1.isSameId(episode2));
        assertFalse(episode1.isSameId(episode3));
        assertFalse(episode2.isSameId(episode3));
    }
    */
}
