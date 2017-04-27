package hills.Anton.delete;

import hills.engine.math.Vec3;
import hills.engine.system.domainModel.TerrainSystem;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TerrainNormalMapCreator {
	
	public TerrainNormalMapCreator(){
		
	}
	
	public static void createFlatNormals(String path){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/main/resources/textures/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < img.getHeight() - 1; y++){
			for(int x = 0; x < img.getWidth() - 1; x++){
				float value = (img.getRGB(x, y) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
				float valueDown = (img.getRGB(x, y + 1) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
				float valueRight = (img.getRGB(x + 1, y) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
				
				Vec3 downVec = new Vec3(0.0f, valueDown - value, 1.0f);
				Vec3 rightVec = new Vec3(1.0f, valueRight - value, 0.0f);
				Vec3 normal = downVec.cross(rightVec).normalize().add(1.0f).div(2.0f);
				
				output.setRGB(x, y, new Color(normal.getX(), normal.getY(), normal.getZ()).getRGB());
			}
		}
		
		try {
			System.out.println("Wrong");
			ImageIO.write(output, "png", new File("src/main/resources/textures/" + path.split("\\.")[0] + "_normal_flat.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createSmoothNormals(String path){
		BufferedImage img = null;
		try {
			ImageInputStream input = ImageIO.createImageInputStream(new File("src/main/resources/textures/" + path));
			img = ImageIO.read(input);
			if(img == null)
				input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		float upValue = 0.0f, leftValue = 0.0f, rightValue = 0.0f, downValue = 0.0f;
		Vec3 upVec = new Vec3(0.0f, 0.0f, 0.0f), leftVec = new Vec3(0.0f, 0.0f, 0.0f), rightVec = new Vec3(0.0f, 0.0f, 0.0f), downVec = new Vec3(0.0f, 0.0f, 0.0f);
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				float value = (img.getRGB(x, y) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
				
				if(y > 0) {
					upValue = (img.getRGB(x, y - 1) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
					upVec = new Vec3(0.0f, upValue - value, -1.0f).normalize();
				}
				
				if(x > 0){
					leftValue = (img.getRGB(x - 1, y) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
					leftVec = new Vec3(-1.0f, leftValue - value, 0.0f).normalize();
				}
				
				if(x < img.getWidth() - 1){
					rightValue = (img.getRGB(x + 1, y) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
					rightVec = new Vec3(1.0f, rightValue - value, 0.0f).normalize();
				}
				
				if(y < img.getHeight() - 1){
					downValue = (img.getRGB(x, y + 1) >> 8 & 0xFF) / 255.0f * TerrainSystem.MAX_HEIGHT;
					downVec = new Vec3(0.0f, downValue - value, 1.0f).normalize();
				}
				
				Vec3 result = new Vec3(0.0f, 0.0f, 0.0f);
				if(y > 0 && x > 0)
					result = result.add(upVec.cross(leftVec));
				
				if(x > 0 && y < img.getHeight() - 1)
					result = result.add(leftVec.cross(downVec));
				
				if(y < img.getHeight() - 1 && x < img.getWidth() - 1)
					result = result.add(downVec.cross(rightVec));
				
				if(x < img.getWidth() - 1 && y > 0)
					result = result.add(rightVec.cross(upVec));
								
				result = result.normalize().add(1.0f).div(2.0f);
				
				output.setRGB(x, y, new Color(result.getX(), result.getY(), result.getZ()).getRGB());
			}
		}
		
		try {
        	File outputFile = new File("src/main/resources/textures/normal.png");// + path.split("\\.")[0] + "_normal_smooth.png");
        	ImageOutputStream out = ImageIO.createImageOutputStream(outputFile);
        	if(out == null)
        		System.out.println("Can't create normal image output stream!");
        	ImageIO.write(output, "png", out);
        	out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
