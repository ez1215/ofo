package com.ofo.data;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSourceAspect {

    private static final Logger log = LoggerFactory.getLogger(DataSourceAspect.class);

    private List<String> slaveMethodPattern = new ArrayList<String>();

    private static final String[] defaultSlaveMethodStart = new String[] {"query","select","find","get"};

    private String [] slaveMethodStart;

    public void setTxAdvice(TransactionInterceptor txAdvice) throws Exception {
        if(txAdvice == null)
            return;
        //从txAdvice中获取事务配置策略
        TransactionAttributeSource transactionAttributeSource = txAdvice.getTransactionAttributeSource();
        if(!(transactionAttributeSource instanceof NameMatchTransactionAttributeSource)){
            return;
        }
        //获取 NameMatchTransactionAttributeSource 对象中的nameMap
        NameMatchTransactionAttributeSource matchTransactionAttributeSource = (NameMatchTransactionAttributeSource) transactionAttributeSource;
        Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class,"nameMap");
        nameMapField.setAccessible(true);
        //获取map的值
        Map<String,TransactionAttribute> map = (Map<String,TransactionAttribute>) nameMapField.get(matchTransactionAttributeSource);

        for (Map.Entry<String,TransactionAttribute> entry : map.entrySet()){
            if (!entry.getValue().isReadOnly()){
                continue;
            }
            slaveMethodPattern.add(entry.getKey());
        }
    }

    public void before(JoinPoint point){
        String methodName = point.getSignature().getName();
        boolean isSlave = false;

        if(slaveMethodPattern.isEmpty()){
            //spring容器中没配置事务策略，采用方法名匹配
            isSlave = isSlave(methodName);
        }else{
            //使用规则匹配
            for (String mapperName : slaveMethodPattern){
                if (isMatch(methodName,mapperName)){
                    isSlave = true;
                    break;
                }
            }
        }
//        log.info("主写副读");
        log.info("执行service方法是【{}】，配置数据源为：{}",methodName,isSlave==true?"从读库【slave】":"主写库【master】");
        if (isSlave) {
            DynamicDataSourceHolder.markSlave();
        }else{
            DynamicDataSourceHolder.markMaster();
        }
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public void setSlaveMethodStart(String[] slaveMethodStart) {
        this.slaveMethodStart = slaveMethodStart;
    }

    public String[] getSlaveMethodStart() {
        if(this.slaveMethodStart == null){
            // 没有指定，使用默认
            return defaultSlaveMethodStart;
        }
        return slaveMethodStart;
    }

    private boolean isSlave(String methodName){
        return StringUtils.startsWithAny(methodName,getSlaveMethodStart());
    }
}
