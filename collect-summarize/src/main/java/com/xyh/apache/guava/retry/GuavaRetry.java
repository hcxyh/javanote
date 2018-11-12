package com.xyh.apache.guava.retry;

/**
 * https://blog.csdn.net/aitangyong/article/details/53886293
 * @author xyh
 *
 */
public class GuavaRetry {
	
	/**
	 * https://juejin.im/post/5b1779bbf265da6e410e0bbb
	 * 之前使用过spring的重试retry
	 */
	
	
	/**
	    * @desc 更新可代理报销人接口
	    * @author jianzhang11
	    * @date 2017/3/31 15:17
	    */
//	   private static Callable<Boolean> updateReimAgentsCall = new Callable<Boolean>() {
//	       @Override
//	       public Boolean call() throws Exception {
//	           String url = ConfigureUtil.get(OaConstants.OA_REIM_AGENT);
//	           String result = HttpMethod.post(url, new ArrayList<BasicNameValuePair>());
//	           if(StringUtils.isEmpty(result)){
//	              throw new RemoteException("获取OA可报销代理人接口异常");
//	           }
//	           List<OAReimAgents> oaReimAgents = JSON.parseArray(result, OAReimAgents.class);
//	           if(CollectionUtils.isNotEmpty(oaReimAgents)){
//	               CacheUtil.put(Constants.REIM_AGENT_KEY,oaReimAgents);
//	               return true;
//	           }
//	           return false;
//	       }
//	   };
	
}
