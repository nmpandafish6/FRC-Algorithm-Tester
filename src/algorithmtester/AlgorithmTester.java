package algorithmtester;

import java.util.Scanner;

public class AlgorithmTester{

    
    static double gyroAngle = 0, staticAngle = 0;
    static double gyroConstant = -.5;
    static Scanner m_scanner;
    
    public static void main(String[] args) {
        while(true){
            m_scanner = new Scanner(System.in);
            System.out.println("Gyro Angle (Deg)\t");
            gyroAngle = m_scanner.nextDouble();
            System.out.println("Joy Y [-1]..[1]\t");
            double joyY = m_scanner.nextDouble();
            System.out.println("Joy X [-1]..[1]\t");
            double joyX = m_scanner.nextDouble();
            double gyroAngleRads = gyroAngle * Math.PI / 180 * gyroConstant;
            double desiredAngle = (Math.atan2(joyY, joyX) + 3*Math.PI/2) % (2*Math.PI);
            double relativeAngle = (-(gyroAngleRads) + (desiredAngle) + (Math.PI/2)) % (2*Math.PI);
            double forward = Math.sin(relativeAngle);
            double strafe = Math.cos(relativeAngle);
            System.out.println("Desired Angle\t" + desiredAngle);
            System.out.println("Relative Angle\t" + relativeAngle);
            //double rotate = joy2.getX();
            double scalar = threshhold(Math.abs(Math.sqrt(sqr(joyX) + sqr(joyY))))/(Math.sqrt(2));
            System.out.println("Scalar \t" + scalar);
            double kP = 0.00277778;
            boolean update = false;

            double rotate = 0;

            double ftLeft = (forward + strafe)*scalar + rotate;
            double ftRight = (-forward + strafe)*scalar + rotate;
            double bkLeft = (forward - strafe)*scalar + rotate;
            double bkRight = (-forward - strafe)*scalar + rotate;

            double output[] = normalize(ftLeft, ftRight, bkLeft, bkRight);

            ftLeft = output[0];
            ftRight = output[1];
            bkLeft = output[2];
            bkRight = output[3];

            System.out.println("Front Left\t" + ftLeft);
            System.out.println("Front Right\t" + ftRight);
            System.out.println("Back Left\t" + bkLeft);
            System.out.println("Back Right\t" + bkRight);
        }
    }
    
    public static double sqr(double value){
        return value*value;
    }
    
    public static double threshhold(double value){
        if(value > 0){
            return Math.min(value, 1);
        }else{
            return Math.max(value, -1);
        }
    }
    
    public static double[] normalize(double value1, double value2, double value3, double value4){
        double[] normalizedValues = new double[4];
        double max = Math.max(Math.abs(value1), Math.abs(value2));
        max = Math.max(Math.abs(value3), max);
        max = Math.max(Math.abs(value4), max);
        System.out.println("MAX\t" + max);
        if(max < 1) {
            normalizedValues[0] = value1;
            normalizedValues[1] = value2;
            normalizedValues[2] = value3;
            normalizedValues[3] = value4;
        }   else    {
        normalizedValues[0] = value1 / max;
        normalizedValues[1] = value2 / max;
        normalizedValues[2] = value3 / max;
        normalizedValues[3] = value4 / max;
        }
        
        return normalizedValues;
    }
    
}