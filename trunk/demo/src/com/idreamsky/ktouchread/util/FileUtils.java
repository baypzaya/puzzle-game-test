package com.idreamsky.ktouchread.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
	public static String convertCodeAndGetText(String str_filepath) {// 转码

		File file = new File(str_filepath);
		BufferedReader reader;
		String text = "";
		try {
			// FileReader f_reader = new FileReader(file);
			// BufferedReader reader = new BufferedReader(f_reader);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3bytes = new byte[3];
			in.read(first3bytes);// 找到文档的前三个字节并自动判断文档类型。
			in.reset();
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
					&& first3bytes[2] == (byte) 0xBF) {// utf-8

				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFE) {

				reader = new BufferedReader(
						new InputStreamReader(in, "unicode"));
			} else if (first3bytes[0] == (byte) 0xFE
					&& first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in,
						"utf-16be"));
			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in,
						"utf-16le"));
			} else {

				reader = new BufferedReader(new InputStreamReader(in, "GBK"));
			}
			String str = reader.readLine();

			while (str != null) {
				text = text + str + "\n";
				str = reader.readLine();

			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	public static void readFile(String path) {
		byte[] t = new byte[2500];
		try {
			File file = new File(path);
			FileInputStream readFile = new FileInputStream(file);
			while (readFile.read(t, 0, 100) != -1) {
				String s = new String(t, 0, 100);
				LogEx.Log_I("string", s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 以下是使用BufferedInputStream实现的：

	public static void readFile2(String path) {
		byte[] data = new byte[1];
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			while (bis.read(data) != -1) {
				String s = new String(data, 0, 1);
				LogEx.Log_I("string", s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("using: java onlyfun caterpillar ");
			e.printStackTrace();
		}
	}
	public static boolean flag = true;
	public static String line="";
	public static void readResource(String path)
	{
		FileInputStream fileStream;
		try {
			fileStream = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader( fileStream));
			StringBuffer sb = new StringBuffer();
			long startTime = System.currentTimeMillis();
//			while (flag) {
//				stringBuffer.append(reader.readLine());
//				LogEx.Log_I("time", stringBuffer.toString()+"");
//			}
			sb = sb.append(reader.readLine()).append("\r\n");   
			int  count =0;
			while (flag) {
//				LogEx.Log_I("time", sb.toString()+"");
				 if(count>=200){
					 sb = sb.delete(0, sb.length()); 
					 count=0;
				 }
				 line = reader.readLine();   
	                if (line == null)
	                {
	                	LogEx.Log_I("time", sb.toString()+"");
	                    break;
	                }
	                sb = sb.append(line);   
				sb.append(line).append("\r\n");
				count++;
			}
			fileStream.close();
			reader.close();
			LogEx.Log_I("time", (System.currentTimeMillis() - startTime)+"");
//			System.out.println("时间:" + (System.currentTimeMillis() - startTime));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// public void readResource(String path) {
	// long fileLength = 0;
	// final int BUFFER_SIZE = 0x300000;// 3M的缓冲
	//        	  
	// for(String fileDirectory:path)//得到文件存放路径，我这里使用了一个方法从XML文件中读出文件的
	// //存放路径，当然也可以用绝对路径来代替这里的fileDriectory
	// {
	// File file = new File(fileDirectory);
	// fileLength = file.length();
	// try {
	// MappedByteBuffer inputBuffer = new
	// RandomAccessFile(file,"r").getChannel().map(FileChannel.MapMode.READ_ONLY,
	// 0, fileLength);//读取大文件
	//        	    
	// byte[] dst = new byte[BUFFER_SIZE];//每次读出3M的内容
	//        	    
	// for(int offset=0; offset < fileLength; offset+= BUFFER_SIZE)
	// {
	// if(fileLength - offset >= BUFFER_SIZE)
	// {
	// for(int i = 0;i < BUFFER_SIZE;i++)
	// dst = inputBuffer.get(offset + i);
	// }
	// else
	// {
	// for(int i = 0;i < fileLength - offset;i++)
	// dst = inputBuffer.get(offset + i);
	// }
	// //将得到的3M内容给Scanner，这里的XXX是指Scanner解析的分隔符
	// Scanner scan = new Scanner(new
	// ByteArrayInputStream(dst)).useDelimiter("XXX");
	// while(scan.hasNext())
	// {
	// //这里为对读取文本解析的方法
	// }
	// scan.close();
	// }
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// }


}
