package EventBus;

/**
 * Created by m.khalid on 5/10/2017.
 */

public class Events
{

    public static class UpdateTextOfDistance {
        private String dis;
        public UpdateTextOfDistance(String dis) {
            this.dis = dis;
        }
        public String getDis() {
            return dis;
        }
    }


}
