package algorithmtester;

import java.util.Scanner;

public class AlgorithmTester{

    
    static double gyroAngle = 0, staticAngle = 0;
    static double gyroConstant = 1;
    static Scanner m_scanner;
    
    public static void main(String[] args) {
        while(true){
            m_scanner = new Scanner(System.in);
            System.out.println("Gyro Angle (Deg)\t");
            gyroAngle = m_scanner.nextDouble();
            System.out.println("Joy1 Y [-1]..[1]\t");
            double joyY = m_scanner.nextDouble();
            System.out.println("Joy1 X [-1]..[1]\t");
            double joyX = m_scanner.nextDouble();
            System.out.println("Joy2 Y [-1]..[1]\t");
            double joy2Y = m_scanner.nextDouble();
            System.out.println("Joy2 X [-1]..[1]\t");
            double joy2X = m_scanner.nextDouble();
            double gyroAngleRads = gyroAngle * Math.PI / 180 * gyroConstant;
            double desiredAngle = (Math.atan2(joyY, joyX) + 3*Math.PI/2) % (2*Math.PI);
            double desiredRotateAngle = (Math.atan2(joy2Y, joy2X) + 3*Math.PI/2) % (2*Math.PI);
            double relativeAngle = (-(gyroAngleRads) + (desiredAngle) + (Math.PI/2)) % (2*Math.PI);
            double forward = Math.sin(relativeAngle);
            double strafe = Math.cos(relativeAngle);
            System.out.println("Desired Angle\t" + desiredRotateAngle);
            System.out.println("Relative Angle\t" + relativeAngle);
            //double rotate = joy2.getX();
            double unscaledJoy[] = {Math.sin(desiredAngle), Math.cos(desiredAngle)};
            double maxJoy[] = normalize(unscaledJoy, true);
            System.out.println(maxJoy[0] + "\t" + maxJoy[1]);
            double scalar = (sqr(joyY) + sqr(joyX)) / (sqr(maxJoy[0]) + sqr(maxJoy[1]));

            System.out.println("Scalar \t" + scalar);
            double kP = 0.1592;

            double error;
            if(((2*Math.PI-(desiredRotateAngle)+(gyroAngleRads))%(2*Math.PI))>Math.PI) {
                error = ((2*Math.PI-(desiredRotateAngle)+(gyroAngleRads))-(2*Math.PI))%(Math.PI);
            } else if(((2*Math.PI-(desiredRotateAngle)+(gyroAngleRads))%(2*Math.PI))<Math.PI){
                error = (2*Math.PI-((2*Math.PI-(desiredRotateAngle)+(gyroAngleRads))))%(Math.PI);
            } else {
                error = Math.PI;
            }
            System.out.println("Error \t" + error);
            double rotate = error * kP;
            System.out.println("Rotate \t" + rotate);

            double ftLeft = (forward + strafe)*scalar + rotate;
            double ftRight = (-forward + strafe)*scalar + rotate;
            double bkLeft = (forward - strafe)*scalar + rotate;
            double bkRight = (-forward - strafe)*scalar + rotate;

            double unnormalizedValues[] = {ftLeft, ftRight, bkLeft, bkRight};
            double output[] = normalize(unnormalizedValues, false);
            
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
    
    public static double[] normalize(double[] values, boolean scaleUp){
        double[] normalizedValues = new double[values.length];
        double max = Math.max(Math.abs(values[0]), Math.abs(values[1]));
        for(int i = 2; i < values.length; i++){
            max = Math.max(Math.abs(values[i]), max);
        }
        if(max < 1 && scaleUp == false) {
            for(int i = 0; i < values.length; i++){
                normalizedValues[i] = values[i];
            }
        }   else    {
            for(int i = 0; i < values.length; i++){
                normalizedValues[i] = values[i] / max;
            }
        }
        
        return normalizedValues;
    }
    
}