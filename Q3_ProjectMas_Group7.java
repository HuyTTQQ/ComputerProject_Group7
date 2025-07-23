package q3_projectmas_group7;

import java.util.*;

public class Q3_ProjectMas_Group7 {

    /**
     * Tính toán điểm va chạm tiếp theo của tia sáng với các biên của hình vuông đơn vị [0,1]x[0,1].
     * 
     * @param a Tọa độ x hiện tại của điểm phản xạ
     * @param b Tọa độ y hiện tại của điểm phản xạ
     * @param angle Góc di chuyển của tia sáng (đơn vị radian)
     * @return Mảng 3 phần tử chứa [x_mới, y_mới, góc_phản_xạ_mới]
     */
    public static double[] firstContact(double a, double b, double angle) { // Tìm điểm va chạm đầu tiên với cạnh
        // Chuẩn hóa góc về khoảng [0, 2π)
        angle = normalizeAngle(angle);
        
        // Tính toán vector đơn vị chỉ hướng di chuyển
        double dx = Math.cos(angle);  // Thành phần x của vector hướng
        double dy = Math.sin(angle);  // Thành phần y của vector hướng

        // Khởi tạo khoảng cách tới điểm va chạm gần nhất
        double t = Double.POSITIVE_INFINITY;
        // Ký hiệu biên sẽ va chạm: L (trái), R (phải), T (trên), B (dưới)
        char boundary = ' ';

        // Kiểm tra va chạm với biên trái (x = 0)
        if (dx < 0) {  // Chỉ xét nếu tia đang di chuyển về phía trái
            double tL = -a / dx;  // Thời gian đến khi chạm biên trái
            double y = b + tL * dy;  // Tọa độ y tại điểm va chạm
            // Kiểm tra điểm va chạm có nằm trong hình vuông và gần hơn điểm trước đó không
            if (y >= 0 && y <= 1 && tL > 0 && tL < t) {
                t = tL;
                boundary = 'L';
            }
        }

        // Kiểm tra va chạm với biên phải (x = 1)
        if (dx > 0) {  // Chỉ xét nếu tia đang di chuyển về phía phải
            double tR = (1 - a) / dx;  // Thời gian đến khi chạm biên phải
            double y = b + tR * dy;  // Tọa độ y tại điểm va chạm
            // Kiểm tra điểm va chạm có nằm trong hình vuông và gần hơn điểm trước đó không
            if (y >= 0 && y <= 1 && tR > 0 && tR < t) {
                t = tR;
                boundary = 'R';
            }
        }

        // Kiểm tra va chạm với biên dưới (y = 0)
        if (dy < 0) {  // Chỉ xét nếu tia đang di chuyển xuống dưới
            double tB = -b / dy;  // Thời gian đến khi chạm biên dưới
            double x = a + tB * dx;  // Tọa độ x tại điểm va chạm
            // Kiểm tra điểm va chạm có nằm trong hình vuông và gần hơn điểm trước đó không
            if (x >= 0 && x <= 1 && tB > 0 && tB < t) {
                t = tB;
                boundary = 'B';
            }
        }

        // Biên trên (y = 1)
        if (dy > 0) {
            double tT = (1 - b) / dy;
            double x = a + tT * dx;
            if (x >= 0 && x <= 1 && tT > 0 && tT < t) {
                t = tT;
                boundary = 'T';
            }
        }

        double c = a + t * dx;
        double d = b + t * dy;

        // Phản xạ: đổi hướng vector
        if (boundary == 'L' || boundary == 'R') {
            dx = -dx;
        } else if (boundary == 'T' || boundary == 'B') {
            dy = -dy;
        }

        double newAngle = Math.atan2(dy, dx);
        newAngle = normalizeAngle(newAngle);

        return new double[]{c, d, newAngle};
    }

    /**
     * Chuẩn hóa một góc về khoảng [0, 2π).
     * 
     * @param angle Góc cần chuẩn hóa (đơn vị radian)
     * @return Góc đã được chuẩn hóa trong khoảng [0, 2π)
     */
    private static double normalizeAngle(double angle) {
        angle = angle % (2 * Math.PI);  // Đưa góc về khoảng (-2π, 2π)
        if (angle < 0) angle += 2 * Math.PI;  // Chuyển về khoảng [0, 2π)
        return angle;
    }

    /**
     * Mô phỏng đường đi của tia sáng phản xạ bên trong gương tròn
     * cho đến khi nó quay trở lại trục hoành.
     * 
     * @param a Tọa độ x ban đầu trên đường tròn (phải nằm trên trục hoành)
     * @param angle Góc ban đầu của tia sáng (đơn vị radian)
     * @return Tọa độ x nơi tia sáng quay trở lại trục hoành
     */
    public static double simulateReturn(double a, double angle) { // Mô phỏng đường đi cho đến khi quay lại trục hoành
        // Khởi tạo vị trí và góc ban đầu
        double x = a;  // Vị trí x ban đầu (trên trục hoành)
        double y = 0;   // Vị trí y ban đầu (trên trục hoành)
        double currentAngle = angle;  // Góc ban đầu của tia sáng

        // Tính điểm phản xạ đầu tiên
        double[] result = firstContact(x, y, currentAngle);
        x = result[0];
        y = result[1];
        currentAngle = result[2];

        // Tiếp tục phản xạ cho đến khi tia sáng rất gần trục hoành
        // (trong phạm vi 1e-10 đơn vị để xử lý sai số số thực)
        while (Math.abs(y) > 1e-10) {
            result = firstContact(x, y, currentAngle);
            x = result[0];
            y = result[1];
            currentAngle = result[2];
        }

        // Trả về tọa độ x nơi tia sáng quay trở lại trục hoành
        return x;
    }

    /**
     * Ước tính giá trị kỳ vọng của R bằng phương pháp Monte Carlo.
     * @param numTrials Số lần thử nghiệm để ước tính
     */
    public static double estimateExpectedR(int numTrials) { // Ước lượng kỳ vọng E(R) bằng Monte Carlo
        Random rand = new Random();
        double sumR = 0;

        // Thực hiện numTrials lần thử nghiệm
        for (int i = 0; i < numTrials; i++) {
            double a = 0.5 * rand.nextDouble();
            // Sinh ngẫu nhiên góc bắn trong khoảng [0, π] radian
            double angle = Math.PI * rand.nextDouble();

            // Mô phỏng đường đi của tia sáng và lấy tọa độ x khi quay về trục hoành
            double r = simulateReturn(a, angle);
            sumR += r;

            // In thông báo tiến độ sau mỗi 10,000 lần thử
            if ((i + 1) % 10000 == 0) {
                System.out.printf("Sau %d lần thử, giá trị E(R) hiện tại = %.6f%n",
                        i + 1, sumR / (i + 1));
            }
        }

        return sumR / numTrials;
    }

    public static void main(String[] args) {
        // Kiểm tra hàm phản xạ
        double a = 0.3;
        double b = 0.4;
        double angle = Math.PI / 4;

        double[] result = firstContact(a, b, angle);
        System.out.printf("Starting at (%.2f, %.2f) with angle %.4f radians%n", a, b, angle);
        System.out.printf("First contact at (%.4f, %.4f), new angle: %.4f radians%n",
                result[0], result[1], result[2]);

        // Ước lượng kỳ vọng E(R)
        int numTrials = 100000;
        System.out.println("\nEstimating E(R) using " + numTrials + " trials...");
        double expectedR = estimateExpectedR(numTrials);
        System.out.printf("Estimated E(R) = %.6f%n", expectedR);
    }
}
