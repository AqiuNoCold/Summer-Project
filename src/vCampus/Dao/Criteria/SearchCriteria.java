package vCampus.Dao.Criteria;

import java.util.HashMap;
import java.util.Map;

public abstract class SearchCriteria {
    protected Map<String, String> criteria = new HashMap<>();
    protected Map<String, String> operators = new HashMap<>(); // 新增：存储每个准则的逻辑运算符

    public void addCriteria(String key, String value, String operator) {
        criteria.put(key, value);
        operators.put(key, operator); // 新增：存储逻辑运算符
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public Map<String, String> getOperators() {
        return operators; // 新增：获取逻辑运算符
    }

    public abstract boolean isValidCriteria(String key);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean firstCondition = true;
        for (Map.Entry<String, String> entry : criteria.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String operator = operators.get(key);
            if (!firstCondition) {
                sb.append(" ").append(operator).append(" ");
            } else {
                firstCondition = false;
            }
            sb.append(key).append(" = '").append(value).append("'");
        }
        return sb.toString();
    }
}