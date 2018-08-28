package demos.dlineage;

/**
 * Created by lcc on 2018/8/28.
 */
public class OracleSQLS {

    public  static  String ORACLE_SQL1 = "create PROCEDURE PROC_UPDATE_COMPOSITERATING AS\n" +
            "  CUR_MONTH NUMBER;\n" +
            "  CURSOR CUR IS\n" +
            "    SELECT -1 FROM DUAL UNION SELECT -2 FROM DUAL UNION SELECT -3 FROM DUAL UNION SELECT -6 FROM DUAL;\n" +
            "BEGIN\n" +
            "  EXECUTE IMMEDIATE 'DELETE FROM RPT_COMPOSITERATING';\n" +
            "  OPEN CUR;\n" +
            "  LOOP\n" +
            "    FETCH CUR\n" +
            "      INTO CUR_MONTH;\n" +
            "    EXIT WHEN CUR%NOTFOUND;\n" +
            "\n" +
            "    INSERT INTO RPT_COMPOSITERATING\n" +
            "      (ID,\n" +
            "       SECUCODE,\n" +
            "       TRADINGCODE,\n" +
            "       SECUABBR,\n" +
            "       STATISTICDATE,\n" +
            "       RATINGAGENCYNUM,\n" +
            "       BUYAGENCYNUM,\n" +
            "       INCREASEAGENCYNUM,\n" +
            "       NEUTRALAGENCYNUM,\n" +
            "       REDUCEAGENCYNUM,\n" +
            "       SELLAGENCYNUM,\n" +
            "       HIGHESTPRICE,\n" +
            "       LOWESTPRICE,\n" +
            "       AVGPRICE,\n" +
            "       MONTH,\n" +
            "       ENTRYTIME,\n" +
            "       UPDATETIME,\n" +
            "       GROUNDTIME,\n" +
            "       UPDATEID,\n" +
            "       RESOURCEID,\n" +
            "       RECORDID,\n" +
            "       PUBDATE)\n" +
            "      SELECT SEQ_ID.NEXTVAL,\n" +
            "             A.SECUCODE,\n" +
            "             A.TRADINGCODE,\n" +
            "             A.SECUABBR,\n" +
            "             TRUNC(SYSDATE) STATISTICDATE,\n" +
            "             A.RATINGAGENCYNUM,\n" +
            "             NVL(A.BUYAGENCYNUM, 0) AS BUYAGENCYNUM,\n" +
            "             NVL(A.INCREASEAGENCYNUM, 0) AS NCREASEAGENCYNUM,\n" +
            "             NVL(A.NEUTRALAGENCYNUM, 0) AS NEUTRALAGENCYNUM,\n" +
            "             NVL(A.REDUCEAGENCYNUM, 0) AS REDUCEAGENCYNUM,\n" +
            "             NVL(A.SELLAGENCYNUM, 0) AS SELLAGENCYNUM,\n" +
            "             NVL(B.HIGHESTPRICE, 0) AS HIGHESTPRICE,\n" +
            "             NVL(B.LOWESTPRICE, 0) AS LOWESTPRICE,\n" +
            "             NVL(B.AVGPRICE, 0) AS AVGPRICE,\n" +
            "             CUR_MONTH*-1 AS MONTH,\n" +
            "             SYSDATE,\n" +
            "             SYSDATE,\n" +
            "             SYSDATE,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             PUBDATE\n" +
            "        FROM (SELECT SECUCODE,\n" +
            "                     TRADINGCODE,\n" +
            "                     SECUABBR,\n" +
            "                     SUM(C) RATINGAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 1, C)) BUYAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 2, C)) INCREASEAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 3, C)) NEUTRALAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 4, C)) REDUCEAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 5, C)) SELLAGENCYNUM\n" +
            "                FROM (SELECT a.SECUCODE,\n" +
            "                             a.TRADINGCODE,\n" +
            "                             b.SECUABBR,\n" +
            "                             INVRATINGCODE,\n" +
            "                             COUNT(*) C\n" +
            "                        FROM TEXT_FORECASTRATING a\n" +
            "                        JOIN pub_securitiesmain b ON a.secucode = b.secucode\n" +
            "                       WHERE TRUNC(PUBDATE) >= ADD_MONTHS(TRUNC(SYSDATE), CUR_MONTH)\n" +
            "                         AND a.SECUCODE != 0\n" +
            "                         AND INVRATINGCODE IN (1, 2, 3, 4, 5)\n" +
            "                       GROUP BY a.SECUCODE,\n" +
            "                                a.TRADINGCODE,\n" +
            "                                b.SECUABBR,\n" +
            "                                INVRATINGCODE)\n" +
            "               GROUP BY SECUCODE, TRADINGCODE, SECUABBR) A,\n" +
            "             (SELECT SECUCODE,MAX(T.PUBDATE) AS PUBDATE,\n" +
            "                     MAX(T.INDEXVAL) HIGHESTPRICE,\n" +
            "                     MIN(T.INDEXVAL) LOWESTPRICE,\n" +
            "                     TRUNC(AVG(T.INDEXVAL), 2) AVGPRICE\n" +
            "                FROM TEXT_PERFORMANCEFORECAST T\n" +
            "               WHERE T.PUBDATE > ADD_MONTHS(TRUNC(SYSDATE), CUR_MONTH)\n" +
            "                 AND T.INDEXCODE = 1190\n" +
            "               GROUP BY SECUCODE) B\n" +
            "       WHERE A.SECUCODE = B.SECUCODE(+);\n" +
            "\n" +
            "  END LOOP;\n" +
            "  CLOSE CUR;\n" +
            "  COMMIT;\n" +
            "END PROC_UPDATE_COMPOSITERATING;\n";

}
