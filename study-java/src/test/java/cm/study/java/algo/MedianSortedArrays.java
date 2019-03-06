package cm.study.java.algo;

import java.util.*;

public class MedianSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int s1 = nums1.length > 0 ? nums1[0] : 0;
        int e1 = nums1.length > 0 ? nums1[nums1.length - 1] : 0;
        int s2 = nums2.length > 0 ? nums2[0] : 0;
        int e2 = nums2.length > 0 ? nums2[nums2.length - 1] : 0;

        int total = (nums1.length + nums2.length);
        int ma = (total - 1) / 2;
        int mb = total / 2;

        int a, b;

        if((e2-e1) * (s2-s1) > 0) {
            if(s2 >= e1) {
                // nums1 在 nums2的左边
                if (nums1.length < nums2.length) {
                    a = nums2[ma - nums1.length];
                    b = nums2[mb - nums1.length];

                } else if (nums1.length > nums2.length) {
                    a = nums1[ma];
                    b = nums1[mb];

                } else {
                    a = nums1[ma];
                    b = nums2[mb - nums1.length];
                }

            } else if (s1 >= e2) {
                // nums2 在 nums1的左边
                if (nums1.length < nums2.length) {
                    a = nums2[ma];
                    b = nums2[mb];

                } else if (nums1.length > nums2.length) {
                    a = nums1[ma - nums2.length];
                    b = nums1[mb - nums2.length];

                } else {
                    a = nums2[ma];
                    b = nums1[mb - nums2.length];
                }

            } else if(s2 > s1){
                // num2与nums1相交, 且num2在右边

            } else { // e2 > s1
                // num2与nums1相交, 且num2在左边

            }


        } else {
            // 相包含
        }

//        return (a+b)/2.0;
        return 0;
    }

    public double zhazha(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) {
            return 0;
        }

        List<Integer> nums = new ArrayList<>();
        for (int n : nums1) {
            nums.add(n);
        }

        for (int n : nums2) {
            nums.add(n);
        }

        Collections.sort(nums, Comparator.comparingInt(e -> e));

        if (nums.size() % 2 == 0) {
            int midIdx1 = nums.size() / 2;
            int midIdx2 = midIdx1 - 1;

            return (nums.get(midIdx1) + nums.get(midIdx2)) / 2.0;
        } else {
            int midIdx = nums.size() / 2;
            return nums.get(midIdx);
        }
    }

    public static void main(String[] args) {
        MedianSortedArrays sortedArrays = new MedianSortedArrays();

        System.out.println("--> " + sortedArrays.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));    // 2
        System.out.println("--> " + sortedArrays.findMedianSortedArrays(new int[]{1, 2}, new int[]{3,4})); // 2.5
        System.out.println("--> " + sortedArrays.findMedianSortedArrays(new int[]{}, new int[]{1})); // 1.0
    }
}
