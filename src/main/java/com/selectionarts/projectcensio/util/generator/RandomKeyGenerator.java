package com.selectionarts.projectcensio.util.generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RandomKeyGenerator {

    private static String UPLOADED_FOLDER = "src/main/resources/public/learningapplication/images/";
    static final String AB = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static String randomString( int len ,String additional){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return shuffle(sb.toString() + additional);
    }

    public static String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
}
