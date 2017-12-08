package cm.study.bigdata.storm.adtrack;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

public class AdTrackTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("track_receiver", new TrackSpout(), 1);
        builder.setBolt("adx_bolt", new AdxBolt(), 4).shuffleGrouping("track_receiver");
        builder.setBolt("dsp_bolt", new DspBolt(), 4).shuffleGrouping("track_receiver");
        builder.setBolt("lp_bolt", new LandPageBolt(), 4).shuffleGrouping("track_receiver");

        Config config = new Config();
        StormSubmitter.submitTopology("ad-tracker", config, builder.createTopology());
    }

}
