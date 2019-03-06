package cm.study.java.algo;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class QuickSorted {

    private static Logger ILOG = LoggerFactory.getLogger(QuickSorted.class);

    public int sorted(int[] nums, int start, int end) {
        if(start < end) {
            int x = partition(nums, start, end);
            sorted(nums, start, x - 1);
            sorted(nums, x + 1, end);

            return 1;
        }

        return 0;
    }

    int partition(int[] nums, int start, int end) {
        int flag = nums[start];
        int x = start;

        for (int i = start + 1, j = end; i <= j; ) {
            if(x > i) {
                if (nums[i] >= flag) {
                    nums[x] = nums[i];
                    x = i;
                }

                i++;
            } else {
                if (nums[j] < flag) {
                    nums[x] = nums[j];
                    x = j;
                }

                j--;
            }
        }

        nums[x] = flag;
        ILOG.info("start: {}, end: {}, flag: {}, x: {}, array: {}", start, end, flag, x, nums);
        return x;
    }

    public static void main(String[] args) {
        int N = 10;
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = RandomUtils.nextInt(1, 20);
        }

//        int[] nums = new int[]{6, 2, 4, 1, 8, 8, 8, 3, 3, 3};
        System.out.println("-->" + Arrays.toString(nums));

        QuickSorted sorted = new QuickSorted();
        sorted.sorted(nums, 0, N-1);
        System.out.println("-->" + Arrays.toString(nums));
    }
}
