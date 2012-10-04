// multiply.java
// Test simple arithmetic
import java.applet.Applet;

public class multiply extends Applet
{
    static int i;
    static int j,k;

    public static void main(String[] args)
    {
	i = 1;
	j = 2;
	k = i * j;
	System.out.println(i + " * " + j + " = " + k);
    }
}