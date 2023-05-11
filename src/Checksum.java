public class Checksum
{
	//Calcolo checksum dato vettore di byte del numero
	public static byte calculateLRC(byte[] data)
	{
		byte checksum=0;
        
        for(int i=0;i<=data.length-1;i++)
        {
            checksum=(byte)((checksum+data[i])&0xFF);
        }
        
        checksum=(byte)(((checksum^0xFF)+1)&0xFF);
        
        return checksum;
    }
	
	//Trasmormazione intero in un vettore di byte
	public static byte[] toBytes(int i)
	{
		byte[] result=new byte[4];
	
		result[0]=(byte)(i>>24);
		result[1]=(byte)(i>>16);
		result[2]=(byte)(i>>8);
		result[3]=(byte)(i/*>>0*/);
		
		return result;
	}
}
