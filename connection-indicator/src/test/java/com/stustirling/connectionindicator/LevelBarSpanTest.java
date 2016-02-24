package com.stustirling.connectionindicator;

import org.junit.Assert;
import org.junit.Test;

public class LevelBarSpanTest {

    @Test
    public void barSpan3Bars3LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(3,3);
        int[] expectedResult = new int[]{1,1,1};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan4Bars4LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(4,4);
        int[] expectedResult = new int[]{1,1,1,1};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan4Bars3LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(3,4);
        int[] expectedResult = new int[]{1,1,2};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan4Bars2LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(2,4);
        int[] expectedResult = new int[]{2,2};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan6Bars4LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(4,6);
        int[] expectedResult = new int[]{1,1,2,2};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan5Bars3LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(3,5);
        int[] expectedResult = new int[]{1,2,2};
        Assert.assertArrayEquals(expectedResult,result);
    }

    @Test
    public void barSpan6Bars5LevelsTest() {
        int[] result = ConnectionIndicatorView.calculateConnectionLevelBarSpans(5,6);
        int[] expectedResult = new int[]{1,1,1,1,2};
        Assert.assertArrayEquals(expectedResult,result);
    }

}
