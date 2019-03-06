package cm.study.java.algo;

public class LongestPalindrome {

    public static class SubStr {
        int start;
        int stop;
        int center;
        int length;
        boolean odd; // 奇个中心

        public SubStr(int start, int stop, int center, int length) {
            this.start = start;
            this.stop = stop;
            this.center = center;
            this.length = length;
            odd = true;
        }
        @Override
        public String toString() {
            return "SubStr{" +
                   "start=" + start +
                   ", stop=" + stop +
                   ", center=" + center +
                   ", length=" + length +
                   ", odd=" + odd +
                   '}';
        }
    }

    public String longestPalindrome(String s) {
        SubStr maxSubStr = new SubStr(0, 0, -1,0);
        SubStr curSubStr = new SubStr(0, 0, -1,0);

        int[] prev = new int[127];
        int[] cur = new int[127];

        for (int i = 0; i < 127; i++) {
            prev[i] = -1;
            cur[i] = -1;
        }

        for (int idx = 0; !s.equals("") && idx < s.length(); idx++) {
            Character ch = s.charAt(idx);
            prev[ch] = cur[ch];
            cur[ch] = idx;

            if(curSubStr.center == -1) { // 追加阶段
                if(prev[ch] >= curSubStr.start) { // 在curSubStr.start之后出现过
                    if(idx - prev[ch] == 1) {
                        // 两个相邻, 开始回朔
                        curSubStr.odd = false;
                        curSubStr.center = prev[ch];

                    } else if (idx - prev[ch] == 2) {
                        curSubStr.odd = true;
                        curSubStr.center = idx - 1;

                    } else {
                        // 修正cur的start位置, 继续尝试; 把start 移到prev[ch]+1
                        curSubStr.start = prev[ch] + 1;
                    }
                }

                // 继续追加
                curSubStr.stop = idx;

            } else { // 回朔阶段
                int mirrorIdx = 2 * curSubStr.center - idx;

                if(mirrorIdx< 0 || ch != s.charAt(mirrorIdx)) {
                    // 找到最近回文
                    int len = 2 * (curSubStr.stop - curSubStr.center) + (curSubStr.odd ? 1 : 2);
                    if (len > maxSubStr.length) {
                        int start = 2 * curSubStr.center - curSubStr.stop + (curSubStr.odd ? 0 : 1);
                        maxSubStr = new SubStr(start, curSubStr.stop, curSubStr.center, len);
                    }

                    curSubStr.start = curSubStr.center + (curSubStr.odd ? 0 : 1);
                    curSubStr.center = -1;

                } else {
                    // 全部回朔完成, 找到当前最大的字串
                    if(idx - curSubStr.center == curSubStr.center - curSubStr.start) {
                        if(curSubStr.length > maxSubStr.length) {
                            maxSubStr = new SubStr(curSubStr.start, curSubStr.stop, curSubStr.center, curSubStr.length);
                        }
                    }
                }

                // 继续追加
                curSubStr.stop = idx;
            }

        }

        if (curSubStr.center > 0) {
            int len = 2 * (curSubStr.stop - curSubStr.center) + (curSubStr.odd ? 1 : 2);
            if (len > maxSubStr.length) {
                maxSubStr.start = 2 * curSubStr.center - curSubStr.stop + (curSubStr.odd ? 0 : 1);
                maxSubStr.stop = curSubStr.stop;
                maxSubStr.center = curSubStr.center;
                maxSubStr.length = len;
            }
        }

        if(maxSubStr.length > 0) {
            return s.substring(maxSubStr.start, maxSubStr.stop + 1);

        } else {
            if(s.length() > 0) {
                // 全部不相同
                return s.substring(0, 1);
            } else {
                // 字符串为空
                return "";
            }
        }
    }

    public static void main(String[] args) {
        LongestPalindrome test = new LongestPalindrome();
//        System.out.println("==> " + test.longestPalindrome(""));
//        System.out.println("==> " + test.longestPalindrome("babad"));
//        System.out.println("==> " + test.longestPalindrome("cbbd"));
//        System.out.println("==> " + test.longestPalindrome("a"));
        System.out.println("==> " + test.longestPalindrome("ccc"));
//        System.out.println("==> " + test.longestPalindrome("abcda"));
    }
}
