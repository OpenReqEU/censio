package com.selectionarts.projectcensio.util.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Properties;

public class KeyGenerator  implements IdentifierGenerator, Configurable {

    //public static final String generatorName = "keyGenerator";

    private String prefix;

    private static String UPLOADED_FOLDER = "src/main/resources/public/learningapplication/images/";
    static final String AB = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();
    private int size = 0;
    private String keypart;

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        StringBuilder sb = new StringBuilder( size );
        for( int i = 0; i < size; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString()+keypart;
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
        size = Integer.parseInt(properties.getProperty("size"));
        keypart = properties.getProperty("key");
    }
}
