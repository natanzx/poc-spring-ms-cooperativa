package br.com.poc.desafio.mock.util;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class RandomUtils {

    public static <T> T pickRandom(final T[] array) {
        if (isEmpty(array)) {
            return null;
        }

        return array[nextInt(0, array.length)];
    }

}
