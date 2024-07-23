package tkaxv7s.xposed.sesame.testRpc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

import tkaxv7s.xposed.sesame.model.task.antForest.AntForestRpcCall;
import tkaxv7s.xposed.sesame.model.task.reserve.ReserveRpcCall;
import tkaxv7s.xposed.sesame.model.task.antOrchard.AntOrchardRpcCall;
import tkaxv7s.xposed.sesame.model.task.antFarm.AntFarmRpcCall;
import tkaxv7s.xposed.sesame.hook.ApplicationHook;
import tkaxv7s.xposed.sesame.util.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TestRPC {
    private static final String TAG = TestRPC.class.getSimpleName();

    @Override
    public String getName() {
        return "Rpc测试";
    }

    public void start(String broadcastFun, String broadcastData, String testType) {
        new Thread() {
            String broadcastFun;
            String broadcastData;
            String testType;

            public Thread setData(String fun, String data, String type) {
                broadcastFun = fun;
                broadcastData = data;
                testType = type;
                return this;
            }

            @Override
            public void run() {
                if ("Rpc".equals(testType)) {
                    String s=test(broadcastFun, broadcastData);
                    Log.debug("收到测试消息:\n方法:" + broadcastFun + "\n数据:" + broadcastData + "\n结果:" + s);
                }
                if ("sendAntdodoAllCard".equals(testType)) {
                    sendAntdodoCard(broadcastFun, broadcastData, true);
                }
                if ("sendAntdodoOneSetCard".equals(testType)) {
                    sendAntdodoCard(broadcastFun, broadcastData, false);
                }

                if ("sendAntdodoOneWholeSetCard".equals(testType)) {
                    sendAntdodoOneWholeSetCard(broadcastFun, broadcastData);
                }
                if ("getNewTreeItems".equals(testType)) {
                    getNewTreeItems();
                }

                if ("collectHistoryAnimal".equals(testType)) {
                    collectHistoryAnimal();
                }

                if ("getWateringLeftTimes".equals(testType)) {
                    getWateringLeftTimes();
                }
                if ("getTreeItems".equals(testType)) {
                    getTreeItems();
                }
                if ("batchHireAnimalRecommend".equals(testType)) {
                    batchHireAnimalRecommend();
                }
                if ("walkGrid".equals(testType)) {
                    walkGrid();
                }
            }
        }.setData(broadcastFun, broadcastData, testType).start();
    }

    private void test(String fun, String data) {
        return ApplicationHook.requestString(fun, data);
    }

    private void sendAntdodoCard(String bookIdInfo, String targetUser, boolean sendAll) {
        try {
            JSONObject jo = new JSONObject(bookIdInfo);
            JSONArray bookIdList = jo.getJSONArray("bookIdList");
            for (int i = 0; i < bookIdList.length(); i++) {
                JSONObject bookInfo = bookIdList.getJSONObject(i);
                if (sendAll) {
                    sendAntdodoAllCard(bookInfo.getString("bookId"), targetUser);
                } else {
                    sendAntdodoOneSetCard(bookInfo.getString("bookId"), targetUser);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntdodoCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private void sendAntdodoAllCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        continue;
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    for (int j = 0; j < count; j++) {
                        jo = new JSONObject(AntForestRpcCall.antdodoSocial(animalId, targetUser));
                        if ("SUCCESS".equals(jo.getString("resultCode"))) {
                            Log.forest("赠送卡片🦕[" + FriendIdMap.getNameById(targetUser) + "]#" + ecosystem + "-" + name);
                        } else {
                            Log.i(TAG, jo.getString("resultDesc"));
                        }
                        Thread.sleep(500L);
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntdodoAllCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private void sendAntdodoOneSetCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        continue;
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    jo = new JSONObject(AntForestRpcCall.antdodoSocial(animalId, targetUser));
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.forest("赠送卡片🦕[" + FriendIdMap.getNameById(targetUser) + "]#" + ecosystem + "-" + name);
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                    Thread.sleep(500L);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntdodoOneSetCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private void sendAntdodoOneWholeSetCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        return;
                }
                for (int j = 0; j < animalForUserList.length(); j++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(j);
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    jo = new JSONObject(AntForestRpcCall.antdodoSocial(animalId, targetUser));
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.forest("赠送卡片🦕[" + FriendIdMap.getNameById(targetUser) + "]#" + ecosystem + "-" + name);
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                    Thread.sleep(500L);

                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntdodoOneWholeSetCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    public String queryEnvironmentCertDetailList(String alias, int pageNum, String targetUserID) {
        return TestRpcCall.queryEnvironmentCertDetailList(alias, pageNum, targetUserID);
    }

    public String sendTree(String certificateId, String friendUserId) {
        return TestRpcCall.sendTree(certificateId, friendUserId);
    }

    private void getNewTreeItems() {
        try {
            String s = ReserveRpcCall.queryTreeItemsForExchange();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray ja = jo.getJSONArray("treeItems");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (!jo.has("projectType"))
                        continue;
                    if (!"TREE".equals(jo.getString("projectType")))
                        continue;
                    if (!"COMING".equals(jo.getString("applyAction")))
                        continue;
                    String projectId = jo.getString("itemId");
                    queryTreeForExchange(projectId);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "getTreeItems err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void queryTreeForExchange(String projectId) {
        try {
            String s = ReserveRpcCall.queryTreeForExchange(projectId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject exchangeableTree = jo.getJSONObject("exchangeableTree");
                int currentBudget = exchangeableTree.getInt("currentBudget");
                String region = exchangeableTree.getString("region");
                String treeName = exchangeableTree.getString("treeName");
                String tips = "不可合种";
                if (exchangeableTree.optBoolean("canCoexchange", false)) {
                    tips = "可以合种-合种类型："
                            + exchangeableTree.getJSONObject("extendInfo").getString("cooperate_template_id_list");
                }
                Log.forest("新树上苗🌱[" + region + "-" + treeName + "]#" + currentBudget + "株-" + tips);
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void collectHistoryAnimal() {
        try {
            String s = AntForestRpcCall.exchangeBenefit("SP20230518000022", "SK20230518000062");
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                antdodoPropList();
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectHistoryAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void antdodoPropList() {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.antdodoPropList());
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray propList = jo.getJSONObject("data").optJSONArray("propList");
                for (int i = 0; i < propList.length(); i++) {
                    JSONObject prop = propList.getJSONObject(i);
                    String propType = prop.getString("propType");
                    if ("COLLECT_HISTORY_ANIMAL_7_DAYS".equals(propType)) {
                        JSONArray propIdList = prop.getJSONArray("propIdList");
                        String propId = propIdList.getString(0);
                        String propName = prop.getJSONObject("propConfig").getString("propName");
                        int holdsNum = prop.optInt("holdsNum", 0);
                        jo = new JSONObject(AntForestRpcCall.antdodoConsumeProp(propId, propType));
                        if ("SUCCESS".equals(jo.getString("resultCode"))) {
                            JSONObject useResult = jo.getJSONObject("data").getJSONObject("useResult");
                            JSONObject animal = useResult.getJSONObject("animal");
                            String ecosystem = animal.getString("ecosystem");
                            String name = animal.getString("name");
                            Log.forest("使用道具🎭[" + propName + "]#" + ecosystem + "-" + name);
                            if (holdsNum > 1) {
                                Thread.sleep(1000L);
                                antdodoPropList();
                                return;
                            }
                        } else {
                            Log.recordLog(jo.getString("resultDesc"), jo.toString());
                        }
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "antdodoPropList err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private void getWateringLeftTimes() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardIndex());
            if ("100".equals(jo.getString("resultCode"))) {
                String taobaoData = jo.getString("taobaoData");
                jo = new JSONObject(taobaoData);
                JSONObject plantInfo = jo.getJSONObject("gameInfo").getJSONObject("plantInfo");
                /*
                 * boolean canExchange = plantInfo.getBoolean("canExchange");
                 * if (canExchange) {
                 * Log.farm("农场果树似乎可以兑换了！");
                 * return;
                 * }
                 */
                JSONObject accountInfo = jo.getJSONObject("gameInfo").getJSONObject("accountInfo");
                int wateringLeftTimes = accountInfo.getInt("wateringLeftTimes");
                Log.farm("今日剩余施肥次数[" + wateringLeftTimes + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "getWateringLeftTimes err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void getTreeItems() {
        try {
            String s = ReserveRpcCall.queryTreeItemsForExchange();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray ja = jo.getJSONArray("treeItems");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (!jo.has("projectType"))
                        continue;
                    if (!"AVAILABLE".equals(jo.getString("applyAction")))
                        continue;
                    String projectId = jo.getString("itemId");
                    String itemName = jo.getString("itemName");
                    getTreeCurrentBudget(projectId, itemName);
                    Thread.sleep(100);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "getTreeItems err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void getTreeCurrentBudget(String projectId, String treeName) {
        try {
            String s = ReserveRpcCall.queryTreeForExchange(projectId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject exchangeableTree = jo.getJSONObject("exchangeableTree");
                int currentBudget = exchangeableTree.getInt("currentBudget");
                String region = exchangeableTree.getString("region");
                Log.forest("树苗查询🌱[" + region + "-" + treeName + "]#剩余:" + currentBudget);
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void batchHireAnimalRecommend() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.batchHireAnimalRecommend(FriendIdMap.getCurrentUid()));
            if ("100".equals(jo.getString("resultCode"))) {
                JSONArray recommendGroupList = jo.optJSONArray("recommendGroupList");
                if (recommendGroupList != null && recommendGroupList.length() > 0) {
                    List<String> GroupList = new ArrayList<>();
                    for (int i = 0; i < recommendGroupList.length(); i++) {
                        jo = recommendGroupList.getJSONObject(i);
                        String animalUserId = jo.getString("animalUserId");
                        int earnManureCount = jo.getInt("earnManureCount");
                        String groupId = jo.getString("groupId");
                        String orchardUserId = jo.getString("orchardUserId");
                        GroupList.add("{\"animalUserId\":\"" + animalUserId + "\",\"earnManureCount\":"
                                + earnManureCount + ",\"groupId\":\"" + groupId + "\",\"orchardUserId\":\""
                                + orchardUserId + "\"}");
                    }
                    if (!GroupList.isEmpty()) {
                        jo = new JSONObject(AntOrchardRpcCall.batchHireAnimal(GroupList));
                        if ("100".equals(jo.getString("resultCode"))) {
                            Log.farm("一键捉鸡🐣[除草]");
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "batchHireAnimalRecommend err:");
            Log.printStackTrace(TAG, t);
        }
    }


    private void walkGrid() {
        try {
            String s = TestRpcCall.walkGrid();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject data = jo.getJSONObject("data");
                if (!data.has("mapAwards"))
                    return;
                JSONArray mapAwards = data.getJSONArray("mapAwards");
                JSONObject mapAward = mapAwards.getJSONObject(0);
                if (mapAward.has("miniGameInfo")) {
                    JSONObject miniGameInfo = mapAward.getJSONObject("miniGameInfo");
                    String gameId = miniGameInfo.getString("gameId");
                    String key = miniGameInfo.getString("key");
                    Thread.sleep(4000L);
                    jo = new JSONObject(TestRpcCall.miniGameFinish(gameId, key));
                    if (jo.getBoolean("success")) {
                        JSONObject miniGamedata = jo.getJSONObject("data");
                        if (miniGamedata.has("adVO")) {
                            JSONObject adVO = miniGamedata.getJSONObject("adVO");
                            if (adVO.has("adBizNo")) {
                                String adBizNo = adVO.getString("adBizNo");
                                jo = new JSONObject(TestRpcCall.taskFinish(adBizNo));
                                if (jo.getBoolean("success")) {
                                    jo = new JSONObject(
                                            TestRpcCall.queryAdFinished(adBizNo, "NEVERLAND_DOUBLE_AWARD_AD"));
                                    if (jo.getBoolean("success")) {

                                    }
                                }
                            }
                        }

                    }
                }
                int leftCount = data.getInt("leftCount");
                if (leftCount > 0) {
                    Thread.sleep(3000L);
                    walkGrid();
                }
            } else {
                Log.recordLog(jo.getString("errorMsg"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "walkGrid err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
