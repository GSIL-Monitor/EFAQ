package com.youku
{
    public class Region
    {
        public var _areaHashMap:Object = {"000000":"其它未知","900000":"美国","990000":"国内未知","520000":"贵州省","360000":"江西省","500000":"重庆市","150000":"内蒙古自治区","420000":"湖北省","430000":"湖南省","220000":"吉林省","350000":"福建省"
                ,"310000":"上海市","110000":"北京市","370000":"山东省","450000":"广西壮族自治区","440000":"广东省","510000":"四川省","340000":"安徽省","620000":"甘肃省","710000":"台湾省","810000":"香港特别行政区","120000":"天津市","320000":"江苏省","210000":"辽宁省"
                ,"140000":"山西省","460000":"海南省","410000":"河南省","610000":"陕西省","130000":"河北省","230000":"黑龙江省","330000":"浙江省","650000":"新疆维吾尔自治区","530000":"云南省","630000":"青海省","640000":"宁夏回族自治区","820000":"澳门特别行政区","540000":"西藏自治区"};
        public var _provinceArray:Array = new Array();
        <PROVINCELIST>
                <PROVINCE id="000000" name="其它未知" /> 
                <PROVINCE id="900000" name="美国" /> 
                <PROVINCE id="990000" name="国内未知" /> 
                <PROVINCE id="520000" name="贵州省" /> 
                <PROVINCE id="360000" name="江西省" /> 
                <PROVINCE id="500000" name="重庆市" /> 
                <PROVINCE id="150000" name="内蒙古自治区" /> 
                <PROVINCE id="420000" name="湖北省" /> 
                <PROVINCE id="430000" name="湖南省" /> 
                <PROVINCE id="220000" name="吉林省" /> 
                <PROVINCE id="350000" name="福建省" /> 
                <PROVINCE id="310000" name="上海市" /> 
                <PROVINCE id="110000" name="北京市" /> 
                <PROVINCE id="370000" name="山东省" /> 
                <PROVINCE id="450000" name="广西壮族自治区" /> 
                <PROVINCE id="440000" name="广东省" /> 
                <PROVINCE id="510000" name="四川省" /> 
                <PROVINCE id="340000" name="安徽省" /> 
                <PROVINCE id="620000" name="甘肃省" /> 
                <PROVINCE id="710000" name="台湾省" /> 
                <PROVINCE id="810000" name="香港特别行政区" /> 
                <PROVINCE id="120000" name="天津市" /> 
                <PROVINCE id="320000" name="江苏省" /> 
                <PROVINCE id="210000" name="辽宁省" /> 
                <PROVINCE id="140000" name="山西省" /> 
                <PROVINCE id="460000" name="海南省" /> 
                <PROVINCE id="410000" name="河南省" /> 
                <PROVINCE id="610000" name="陕西省" /> 
                <PROVINCE id="130000" name="河北省" /> 
                <PROVINCE id="230000" name="黑龙江省" /> 
                <PROVINCE id="330000" name="浙江省" /> 
                <PROVINCE id="650000" name="新疆维吾尔自治区" /> 
                <PROVINCE id="530000" name="云南省" /> 
                <PROVINCE id="630000" name="青海省" /> 
                <PROVINCE id="640000" name="宁夏回族自治区" /> 
                <PROVINCE id="820000" name="澳门特别行政区" /> 
                <PROVINCE id="540000" name="西藏自治区" /> 
            </PROVINCELIST>
        public function Region() {
        }
        
        public function getAreaList():void {
            _provinceArray.push("Select Area");
            _provinceArray.push("ALL");
            _provinceArray.push("北京市");
            _provinceArray.push("上海市");
            _provinceArray.push("重庆市");
            _provinceArray.push("天津市");
            _provinceArray.push("广东省");
            _provinceArray.push("浙江省");
            _provinceArray.push("福建省");
            _provinceArray.push("山东省");
            for(var str:String in _areaHashMap){
//                trace(str+" "+_areaHashMap[str]);
                if(_provinceArray.indexOf(str)!= -1) continue;
                _provinceArray.push(_areaHashMap[str]);
            }
        }
    }
}

