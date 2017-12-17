

import java.util.Random;
 
public class RandomPermutationGenerator {
    private int K;
    private int[] nums;
 
    public RandomPermutationGenerator(int K, int M) {
        this.K = K;
        nums = new int[M + 1];
        for (int i = 1; i <= M; i++) {
            nums[i] = i;
        }
    }
 
    public int[][] generateRandomPerumations() {
        int[][] perm = new int[K][nums.length];
        perm[0] = nums;
        for (int i = 1; i < K; i++) {
            perm[i] = shuffleArray(perm[i - 1]);
        }
        return perm;
    }
 
    public static int[] shuffleArray(int[] nums) {
        int[] array = new int[nums.length];
        System.arraycopy(nums, 0, array, 0, nums.length);
        Random rgen = new Random();
 
        for (int i = 0; i < array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
 
        return array;
    }
}
