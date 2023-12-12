package ink.organics.pojo2json.parser.el;

import java.util.Random;

public interface RandomTypeValue extends PresetTypeValue {

    Random random = new Random();

    Object getRandomValue();
}
