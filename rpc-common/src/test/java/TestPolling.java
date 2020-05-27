import java.util.ArrayList;
import java.util.List;

public class TestPolling {
    public static void main(String[] args) {
        List<Integer> timeList=new ArrayList<>();
        timeList.add(1);
        timeList.add(3);
        timeList.add(5);
        timeList.add(7);
        timeList.add(9);
        timeList.add(11);
        timeList.add(13);
        System.out.println(binarySearch(timeList,9));
    }

    private static int binarySearch(List<Integer> timeList, int pos) {
        int high = timeList.size() - 1;
        int low = 0;
        int mid = 0;
        // 取大于等于pos的第一个元素
        while (low < high) {
            mid = (high + low) >> 1;
            //小于的话不能取直接排除
            if (timeList.get(mid) < pos) {
                low = mid+1;
            } else if(timeList.get(mid)> pos){
                high = mid;
            }else{
                break;
            }
        }
        return mid;
    }
}
