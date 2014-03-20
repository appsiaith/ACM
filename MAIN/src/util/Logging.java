package util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 28/02/2014
 * Time: 11:18
 */
public class Logging {
    public static Logger logger = Logger.getLogger("view.Main.class");

    static {
        BasicConfigurator.configure();
    }

    public static void info(String info) {
        logger.info(info);
    }

    public static void error(String info) {
        logger.error(info);
    }
}
