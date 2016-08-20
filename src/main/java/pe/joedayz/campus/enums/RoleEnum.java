package pe.joedayz.campus.enums;


import java.util.ArrayList;
import java.util.List;

public enum RoleEnum {
    SYSTEM("ADMIN","System Administrator"),
    PRICING_ANALYST("PRIAN","Pricing Analyst"),
    SALES_ACCOUNT_MANAGER("SAACM","Sales Account Manager"),
    PRICING_MANAGER("PRIMA","Pricing Manager"),
    BLOCKED_AT_OFFICE("BAOFF","Blocked At Office"),
    ;

    private String code;
    private String label;

    RoleEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static List<RoleEnum> findAll(){
        List<RoleEnum> list= new ArrayList<RoleEnum>();

        for(RoleEnum status: RoleEnum.values()){
            list.add(status);
        }

        return list;
    }

    public static RoleEnum findByCode(String value){
        RoleEnum[] array= RoleEnum.values();
        RoleEnum result=null;
        for (int i=0;i<array.length;i++){
            if(array[i].getCode().equals(value)){
                result=array[i];
                break;
            }
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
