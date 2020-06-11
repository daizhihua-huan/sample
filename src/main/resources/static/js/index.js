/**
 * Created by Administrator on 2017/5/17.
 */

$(function(){




    // index();
    $(".index_nav ul li").each(function(index){
        $(this).click(function(){
            $(this).addClass("nav_active").siblings().removeClass("nav_active");
            $(".index_tabs .inner").eq(index).fadeIn().siblings("div").stop().hide();
            if(index==0){
                console.log(index);
                getdtree(layui.dtree,index)
            }else if(index==1){
                console.log(1);
                getdtree(layui.dtree,index)
            }else if(index==2){
                console.log(2)

            }else if(index==3){
                $(".spanYear").html($("#date").val());
                AnQuan();
            }else if(index==4){

                user();
            }else if(index==5){
                $(".spanYear").html($("#date").val());
                manage();
            }
        })
    });
    $(".tabs ul li").each(function(index){
       $(this).click(function(){
           $(".tabs ul li div .div").removeClass("tabs_active");
           $(this).find("div .div").addClass("tabs_active");
           $(".tabs_map>div").eq(index).fadeIn().siblings("div").stop().hide();
       })
   });
    $(".middle_top_bot ul li").each(function(){
        $(this).click(function(){
            $(".middle_top_bot ul li").removeClass("middle_top_bot_active");
            $(this).addClass("middle_top_bot_active");
        })
    });

});

layui.extend({
    dtree: '../static/layui/layui_ext/dtree/dtree'
}).use(['upload','element','dtree','laydate'], function(){
    var $ = layui.jquery
    upload = layui.upload,element = layui.element;
    var dtree = layui.dtree;
    var laydate = layui.laydate;
    //常规用法yyyy年MM月dd日HH时mm分ss秒
    getdtree(dtree,0);
    $.ax("/getDicForPid",
        {pid: '1'},
        null,
        null,
        null,
        function(res){
            layui.dtree.render({
                elem: "#toolbarDiv2",
                data: res.data,
                record:true,
                checkbar: true,
                checkbarType: "only", // 默认就是all，其他的值为： no-all  p-casc   self  only
                checkbarFun: {
                    chooseDone: function(checkbarNodesParam) { //复选框点击事件完毕后，返回该树关于复选框操作的全部信息。
                        console.log(checkbarNodesParam[0]);
                        var data = checkbarNodesParam[0];
                        if(data.context=='全市'){
                            $("#cityregion").attr("style","display:none;")
                            $("#citypoint").attr("style","display:block")
                        }else{
                            $("#cityregion").attr("style","display:block;")
                            $("#citypoint").attr("style","display:none");
                            $("#citynutrient").text(data.context+"养分综合分级各等级占比统计图");
                            $("#cityenvironment").text(data.context+"环境综合分级各等级占比统计图");
                            $("#cityland").text(data.context+"土地利用类型监测点数量统计图");
                            getregionalism(data);
                        }
                    }
                }
            });
        },
        function(error){
            alert("出错了");
        }
    );



    laydate.render({
        elem: '#date'
        ,type: 'year'
        ,max: new Date().getTime()
        // ,value: new Date()
        ,isInitValue: true
        ,done:function (value) {
            var elem = "toolbarDiv";
            var type = "区域监测";
            if(indexflag==1){
                elem = "toolbarDiv1";
                type="重点监测";
            }
            var params = dtree.getCheckbarNodesParam(elem);
            console.log(params)
            var data = {
                type:type,
                year: value
            };
            if(params.length>0){
                data.name = params[0].recordData.dataCode;
                data.index = params[0].recordData.dataIndex;
            }
            getajax(data,0)
        }
        ,finally:function () {

        }
    });
    laydate.render({
        elem: '#yearDate'
        , type: 'year'
        , max: new Date().getTime()
        , value: new Date()
        , isInitValue: true
        , done: function (value) {

        }
        , finally: function () {

        }
    });

    //重点工区的上传
    laydate.render({
        elem: '#yearPoint'
        , type: 'year'
        , max: new Date().getTime()
        , value: new Date()
        , isInitValue: true
        , done: function (value) {

        }
        , finally: function () {

        }
    });
    var msg ;
    //选完文件后不自动上传
    upload.render({
        elem: '#test8'
        ,url: '/sendFile' //改成您自己的上传接口
        ,auto: false
        ,accept:'file'
        ,exts:'xls|xlsx'
        ,field:'file'
        //,multiple: true
        ,bindAction: '#test9'
        ,before:function (res) {
            msg = layer.msg('正在保存数据...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false})
        }
        ,progress: function(n, elem) {
            console.log(elem)
            var percent = n + '%' //获取进度百分比
            console.log(percent)
            element.progress('demo', percent); //可配合 layui 进度条元素使用
        }
        ,done: function(res){
            console.log(res)
            if(res.status==201){
                layer.msg(res.data,{time: 1200},function () {
                    element.progress('demo', '0%'); //可配合 layui 进度条元素使用
                });
                layer.close(msg);
            }else if(res.status==202){
                layer.close(msg);
                var confirm = layer.confirm('编码重复,是否更新数据？', {
                    btn: ['是','否'] //按钮
                }, function(){
                    // layer.msg('的确很重要', {icon: 1});
                    layer.close(confirm)
                    var index;
                    jQuery.axgetbefore("/updateData","",function () {
                        index = layer.msg('正在更新数据...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false})
                    },function (res) {
                        layer.msg(res.data,{time: 1200},function () {
                            element.progress('demo', '0%'); //可配合 layui 进度条元素使用
                        });
                    },function (error) {
                        layer.msg(error,{time: 1200},function () {
                            element.progress('demo', '0%'); //可配合 layui 进度条元素使用
                        });
                    },function () {
                        layer.close(index);
                    })
                }, function(){
                    element.progress('demo', '0%'); //可配合 layui 进度条元素使用
                });
            }else {
                layer.msg(res.data,{time: 1200},function () {
                    element.progress('demo', '0%'); //可配合 layui 进度条元素使用
                });
                layer.close(msg);
            }



        }
    });
    //选完文件后不自动上传
    upload.render({
        elem: '#image1'
        , url: '/sendImageFile' //改成您自己的上传接口
        , accept: 'images'
        , field: 'image'
        ,auto: false
        , bindAction: '#imageupload'
        , before:function (res) {
            this.data={
                type:'0',
                year:$("#yearDate").val(),
                pid:'0'
            }

        }
        , progress: function (n, elem) {
            console.log(elem)
            var percent = n + '%' //获取进度百分比
            console.log(percent)
            element.progress('imageplan', percent); //可配合 layui 进度条元素使用

        }
        , done: function (res) {
            console.log(res);

            layer.msg('上传成功',{time: 1800},function () {
                element.progress('imageplan', '0%'); //可配合 layui 进度条元素使用
            });

        },
    });

});

function getCity(map) {
    map.plugin('AMap.DistrictSearch', function () {
        // 创建行政区查询对象
        var district = new AMap.DistrictSearch({
            // 返回行政区边界坐标等具体信息
            extensions: 'all',
            // 设置查询行政区级别为 区
            level: 'district'
        })
        var fillOpacity = 0.1;
        district.search('朝阳区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('东城区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('西城区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('房山区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('海淀区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('丰台区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('石景山区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('门头沟区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('通州区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('延庆区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('密云区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('昌平区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('怀柔区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('顺义区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('平谷区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
        district.search('大兴区', function(status, result) {
            // 获取朝阳区的边界信息
            var bounds = result.districtList[0].boundaries
            var polygons = []
            if (bounds) {
                for (var i = 0, l = bounds.length; i < l; i++) {
                    //生成行政区划polygon
                    var polygon = new AMap.Polygon({
                        map: map,
                        strokeWeight: 1,
                        path: bounds[i],
                        fillOpacity: fillOpacity,
                        fillColor: '#CCF3FF',
                        strokeColor: '#0000FF'
                    })
                    polygons.push(polygon)
                }
                // 地图自适应
                map.setFitView()
            }
        })
    })
}

function getdtree(dtree,index){
    $.ax("/getDicer",
        null,
        null,
        null,
        null,
        function(res){
            console.log(res.data)
            var elem = "#toolbarDiv";
            var type = "区域监测";
            if(index==1){
                elem = "#toolbarDiv1";
                type = "重点监测";
            }
            var data = {
                type:type
            };
            if(typeof ($("#date").val())!="undefined"){
                data.year = $("#date").val();
            }


            getajax(data,index);
            var DTree = dtree.render({
                elem: elem,
                data: res.data,
                defaultRequest:{
                    dataCode:'dataCode',
                    dataIndex:'dataIndex'
                },
                record:true,
                checkbar: true,
                initLevel: 1,
                accordion: true,  // 开启手风琴
                checkbarType: "only", // 默认就是all，其他的值为： no-all  p-casc   self  only
                checkbarFun: {
                    chooseDone: function(checkbarNodesParam) { //复选框点击事件完毕后，返回该树关于复选框操作的全部信息。
                        console.log(checkbarNodesParam[0])
                        if(index==0||typeof (indexflag)=='undefined'){

                            getajax({name:checkbarNodesParam[0].recordData.dataCode,
                                index:checkbarNodesParam[0].recordData.dataIndex,
                                type:'区域监测',year:$("#date").val()},0)
                            return;
                        }else{
                            getajax({name:checkbarNodesParam[0].recordData.dataCode,
                                index:checkbarNodesParam[0].recordData.dataIndex,
                                type:'重点监测',year:$("#date").val()},index)
                            return;
                        }

                    }
                }
            });
        },
        function(error){
            alert("出错了");
        }
    );
}


function getajax(data,index){
    var map;
    if(index==0){
        map = new AMap.Map('container', {
            zoom: 11,
            center: [116.397428, 39.90923],
            resizeEnable: true,
            mapStyle: 'amap://styles/5e5e1d41627490a3db3784528d6e545a'
        });
        AMap.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView'],function(){
            map.addControl(new AMap.ToolBar());
            map.addControl(new AMap.Scale());
        })
    }else{
        map = new AMap.Map('container1', {
            zoom: 11,
            center: [116.397428, 39.90923],
            resizeEnable: true,
            mapStyle: 'amap://styles/5e5e1d41627490a3db3784528d6e545a'
        });
        AMap.plugin(['AMap.ToolBar','AMap.Scale'],function(){
            map.addControl(new AMap.ToolBar());
            map.addControl(new AMap.Scale());
        })
    }
    var index;
    $.axget("/getData",data,function(){
        console.log('123123213');
        index = layer.msg('请稍后...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false})
        },function (res) {
        console.log(res);
        var mass;
        if(typeof (res.data.years)!='undefined'||res.data.years!=''){
            $("#date").val(res.data.years);
        }

        getCity(map);

        var style = [{
            url: '../static/images/icon0.png',
            anchor: new AMap.Pixel(4, 4),
            size: new AMap.Size(30, 30),
        }, {
            url: '../static/images/icon1.png',
            anchor: new AMap.Pixel(4, 4),
            size: new AMap.Size(30, 30)
        }, {
            url: '../static/images/icon2.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(30, 30)
        },{
            url: '../static/images/icon3.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(30, 30)
        }, {
            url: '../static/images/icon4.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(30, 30)
        }];

        if(index==0){
            $("#imagerview").empty();
            var html="";
            for (var i = 0; i <res.data.listName.length ; i++) {
                html+= "<div class=\"input-item\">" +
                    "     <span>"+res.data.listName[i].name+"</span>" +
                    "      <div>\n" +
                    "      <img src="+res.data.listName[i].url+">\n" +
                    "      </div>\n" +
                    "  </div>";
            }
            $("#imagerview").append(html);
        }else{
            $("#typeimage").empty();
            var html="";
            for (var i = 0; i <res.data.listName.length ; i++) {
                html+= "<div class=\"input-item\">" +
                    "     <span>"+res.data.listName[i].name+"</span>" +
                    "      <div>\n" +
                    "      <img src="+res.data.listName[i].url+">\n" +
                    "      </div>\n" +
                    "  </div>";
            }
            $("#typeimage").append(html);
        }
        console.log('-----------------------'+map)

        mass = new AMap.MassMarks(res.data.data,{
            opacity: 1,
            zIndex: 111,
            cursor: 'pointer',
            style: style
        });
        mass.setMap(map);

        mass.on('mouseout',function (e) {
            infoWindow.close();
        });
        var infoWindow = new AMap.InfoWindow({
            offset: new AMap.Pixel(0, 0),
            isCustom:true,
            autoMove:false,
            closeWhenClickMap:false

        });
        mass.on('mouseover', function (e) {
            console.log(e)
            var html = "<div class=\"make\">\n" +
                "    <label>编号：<span>"+e.data.samplecode+"</span></label>\n" +
                "    <div style=\"border-top:1px dashed #cccccc;height: 1px;overflow:hidden\"></div>\n" +
                "    <div style=\" display: inline;\n" +
                "        flex-direction: row;\">\n" +
                "        <label>经度：<span>"+e.data.xx+"</span></label>\n" +
                "        <label>纬度：<span>"+e.data.yy+"</span></label>\n" +
                "    </div>\n" +
                "    <div style=\"border-top:1px dashed #cccccc;height: 1px;overflow:hidden\"></div>\n" +
                "    <div style=\" display: inline;\n" +
                "        flex-direction: row;\">\n" +
                "        <label>区：<span>"+e.data.street+"</span></label>\n" +
                "        <label>乡（镇）：<span>"+e.data.community+"</span></label>\n" +
                "    </div>\n" +
                "    <div style=\"border-top:1px dashed #cccccc;height: 1px;overflow:hidden\"></div>\n" +
                "    <div style=\" display: inline;\n" +
                "        flex-direction: row;\">\n" +
                "        <label>土地利用：<span>"+e.data.utilization+"</span></label>\n" +
                "        <label>土壤质地：<span>"+e.data.solitype+"</span></label>\n" +
                "    </div>\n" +
                "    <div style=\"border-top:1px dashed #cccccc;height: 1px;overflow:hidden\"></div>\n" +
                "    <label>位置：<span>"+e.data.site+"</span></label>\n" +
                "</div>";
            infoWindow.setContent(html);
            infoWindow.open(map, e.data.lnglat);
        });


    },function (error) {
        alert("出错了");
    },function () {
        layer.close(index);
    })
}

//重点工区的方法
function user(){
    $(".spanYear").html($("#date").val());
    $.axget("/getDicImgType",{pid:'2'},function(){

    },function (res) {
        getImage('46');
        layui.dtree.render({
            elem: "#toolbarDiv3",
            data: res.data,
            record:true,
            checkbar: true,
            initLevel: 1,
            accordion: true,  // 开启手风琴
            checkbarType: "only", // 默认就是all，其他的值为： no-all  p-casc   self  only
            checkbarFun: {
                chooseDone: function(checkbarNodesParam) { //复选框点击事件完毕后，返回该树关于复选框操作的全部信息。
                    console.log(checkbarNodesParam[0]);
                    getImage(checkbarNodesParam[0].nodeId);
                }
            }
        });
    },function (error) {

    },function () {
        
    })


}

function getImage(pid) {
    $.axget("/getImageFile",{'pid':pid,'type':'1',year:$("#date").val()},function(){

    },function (res) {
        console.log(res);
        var list = res.data;

        $("#brokenlineimg").attr('src','');
        $("#formimage").attr("src",'');
        $("#gradingImage").attr("src",'');
        $("#environmentContainer2").attr("src",'');
        for(var i=0;i<list.length;i++){
            switch (i) {
                case 0:
                    var path = list[i].path
                    $.axgetimage("/getImageFileType",{path:list[i].path},function (res) {
                        if(res.length<=0){
                            // $('#imagecity').hide();
                            return;
                        }
                       console.log(path);
                        $("#brokenlineimg").attr("src","/getImageFileType?path="+encodeURIComponent(path));
                    },function (error) {
                        console.log(error);
                    })
                    break;
                case 1:
                    $("#formimage").attr("src","/getImageFileType?path="+encodeURIComponent(list[i].path));
                    break;
                case 2:
                    $("#gradingImage").attr("src","/getImageFileType?path="+encodeURIComponent(list[i].path));
                    break;
                case 3:
                    $("#environmentContainer2").attr("src","/getImageFileType?path="+encodeURIComponent(list[i].path));
                    break;

            }

        }
    },function (error) {

    },function () {
        
    })

}



function manage(){
    $.axget("/getDicForPid",{pid:'2'},function(){

    },function (res) {
        console.log(res);
        $('#quiz1').empty();
        $.each(res.data, function (index, item) {
            //"<option value="+item.value+">"+item.name+"</option>")new Option(item.dataCode, item.id)
            // $('#quiz1').append("<option value="+item.id+">"+item.dataCode+"</option>");// 下拉菜单里添加元素
            $('#quiz1').append(new Option(item.dataCode, item.id));// 下拉菜单里添加元素
        });
        layui.form.render("select");//重新渲染 固定写法
        $.axget("/getDicForPid",{pid:'42'},function(){

        },function (res) {
            $.each(res.data, function (index, item) {
                $('#quiz2').append(new Option(item.title, item.id));// 下拉菜单里添加元素
            });
            layui.form.render("select");//重新渲染 固定写法
            city('42');
        },function (error) {

        },function () {
            
        })



    },function (error) {

    },function () {
        
    })

    $.axget("/getDicForPid",{pid:"3"},function(){

    },function (res) {
        console.log(res);
        $('#imgtype').empty();
        $.each(res.data, function (index, item) {
            //"<option value="+item.value+">"+item.name+"</option>")new Option(item.dataCode, item.id)
            // $('#quiz1').append("<option value="+item.id+">"+item.dataCode+"</option>");// 下拉菜单里添加元素
            $('#imgtype').append(new Option(item.title, item.dataCode));// 下拉菜单里添加元素
        });
    },function (error) {

    },function () {
        
    })



}



function AnQuan() {
    console.log('----------------------------')
     var srcName ="/getImage?type=0&year="+$("#date").val()
    $.axgetimage("/getImage",{type:'0',year:$("#date").val()},function (res) {
        console.log(res);
        if(res.length<=0){
            $('#imagecity').hide();
            return;
        }
        $('#imagecity').attr("src",srcName)

    },function (error) {
        console.log(error)
    })


    $.axget("/getDataForByType",{type:'区域监测',year:$("#date").val()},function(){

    },function (res) {
        console.log(res);
        var data = res.data;
        var charts = Highcharts.chart('administrativeContainer', {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
            },
            title: {
                text: null
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: '数量'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        // format: '{point.y:.1f}'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: 占比例为<b>{point.number}</b> <br/>'
            },
            series: [{
                name: '行政区域',
                colorByPoint: true,
                data: data,
            }]
        });
    },function (error) {

    },function () {
        
    });

    $.axget("/getDataForByTypeNutrient",{type:'区域监测',year:$("#date").val()},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('nutrientContainer', {
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {

                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                }
            },
            series: [{
                type: 'pie',
                name: '养分综合占比',
                innerSize: '50%',
                data: data
            }]
        });
    },function (error) {

    },function () {
        
    })

    $.axget("/getDataForByEnvironmentContainer",{type:'区域监测',year:$("#date").val()},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('environmentContainer', {
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                }
            },
            series: [{
                type: 'pie',
                name: '环境综合占比',
                innerSize: '50%',
                data: data
            }]
        });
    },function (error) {

    },function () {
        
    })

  /*  $.axget("/getHealthElements",{type:'区域监测',year:$("#date").val(),pid:'28'},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('healthElements', {
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                }
            },
            series: [{
                type: 'pie',
                name: '环境综合占比',
                innerSize: '50%',
                data: data
            }]
        });
    },function (error) {

    },function () {
        
    })*/

    $.axget("/getComprehensive",{type:'区域监测',year:$("#date").val()},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('comprehensive', {
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                }
            },
            series: [{
                type: 'pie',
                name: '环境综合占比',
                innerSize: '50%',
                data: data
            }]
        });
    },function (error) {

    },function () {
        
    })


    $.axget("/getLandTypeContainer",{type:'区域监测',year:$("#date").val()},function(){

    },function (res) {
        console.log(res);
        var data = res.data;
        var charts = Highcharts.chart('landTypeContainer', {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
            },
            title: {
                text: null
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: '数量'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        // format: '{point.y:.1f}'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: 占比例为<b>{point.number}</b> <br/>'
            },
            series: [{
                name: '行政区域',
                colorByPoint: true,
                data: data,
            }]
        });



    },function (error) {

    },function () {
        
    });


    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'26',index:'1'},function(){

    },function (res) {
        console.log(res);
        var data = res.data;
        Highcharts.chart('environmentalHealthContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: data.listName
            },
            yAxis: {
                min: 0,
                title: {
                    text: '环境指标元素'
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>' +
                    '({point.percentage:.0f}%)<br/>',
                //:.0f 表示保留 0 位小数，详见教程：https://www.hcharts.cn/docs/basic-labels-string-formatting
                shared: true
            },
            plotOptions: {
                column: {
                    stacking: 'percent'
                }
            },
            series: data.list
        });

    },function (error) {

    },function () {
        
    })

    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'27'},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('nutrientIndicatorsContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: data.listName
            },
            yAxis: {
                min: 0,
                title: {
                    text: '养分指标元素'
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>' +
                    '({point.percentage:.0f}%)<br/>',
                //:.0f 表示保留 0 位小数，详见教程：https://www.hcharts.cn/docs/basic-labels-string-formatting
                shared: true
            },
            plotOptions: {
                column: {
                    stacking: 'percent'
                }
            },
            series: data.list
        });
    },function (error) {

    },function () {
        
    })


    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'28'},function(){

    },function (res) {
        var data = res.data;
        var chart = Highcharts.chart('soundDevelopmentContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: null
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: data.listName
            },
            yAxis: {
                min: 0,
                title: {
                    text: '养分指标元素'
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>' +
                    '({point.percentage:.0f}%)<br/>',
                //:.0f 表示保留 0 位小数，详见教程：https://www.hcharts.cn/docs/basic-labels-string-formatting
                shared: true
            },
            plotOptions: {
                column: {
                    stacking: 'percent'
                }
            },
            series: data.list
        });

    },function (error) {

    },function () {
        
    })



}

function getregionalism(data) {
    console.log(data);
    $.axget("/getNutrientForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function(){

    },function (res) {
        var list = res.data;
        //citynutrientContainer
        console.log(list)
        var chart = Highcharts.chart('citynutrientContainer', {
            title: {
                text: '养分综合<br>占比',
                align: 'center',
                verticalAlign: 'middle',
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                    // center: ['50%', '75%']
                }
            },
            series: [{
                type: 'pie',
                name: '养分综合占比',
                innerSize: '50%',
                data: list
            }]
        });

    },function (error) {
        console.log(error)
    },function () {
        
    })
    $.axget("/getEnvironmentForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function(){

    },function (res) {
        var list = res.data;
        //citynutrientContainer
        console.log(list)
        var chart = Highcharts.chart('cityenvironmentContainer', {
            title: {
                text: '环境综合<br>占比',
                align: 'center',
                verticalAlign: 'middle',
            },
            credits: {
                enabled: false
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    showInLegend: false,
                    cursor: 'pointer',
                    dataLabels: {
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        showInLegend: true,
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            // color: 'white',
                            textShadow: '0px 1px 1px black'
                        }
                    },
                    startAngle: -180, // 圆环的开始角度
                    endAngle: 180,    // 圆环的结束角度
                }
            },
            series: [{
                type: 'pie',
                name: '环境综合占比',
                innerSize: '50%',
                data: list
            }]
        });

    },function (error) {
        console.log(error)
    },function () {
        
    })

    $.axget("/getLandTypeForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function(){

    },function (res) {
        console.log(res);
        var data = res.data;
        var charts = Highcharts.chart('landTypeContainerContainer', {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
            },
            title: {
                text: null
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: '数量'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        // format: '{point.y:.1f}'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
               /* pointFormat: '<span style="color:{point.color}">{point.name}</span>: 占比例为<b>{point.number}</b> <br/>'*/
            },
            series: [{
                name: '行政区域',
                colorByPoint: true,
                data: data,
            }]
        });



    },function (error) {

    },function () {
        
    });

}

function yingXiao(){

}

function shouRu(){

}


function city(pid) {
    $('#quiz2').empty();
    $.axget("/getDicForPid",{pid:pid},function(){

    },function (res) {
        var pid = $("select[name='quiz2']").val();
        $.each(res.data, function (index, item) {
            $('#quiz2').append(new Option(item.title, item.id));// 下拉菜单里添加元素
        });
        layui.form.render("select");//重新渲染 固定写法
        layui.use(['upload','form'], function(){
            layui.form.on('select(quiz1-dropdown)', function(data){
                city(data.value);
                // this.getHousing(data);
            });
            var pid;
            layui.form.on('select(quiz2-dropdown)', function(data){
                console.log(data);
                // this.getHousing(data);
            });
            var upload = layui.upload;
            var element = layui.element;
            //选完文件后不自动上传
            upload.render({
                elem: '#image2'
                , url: '/sendImageFile' //改成您自己的上传接口
                , accept: 'images'
                , field: 'image'
                ,auto: false
                , bindAction: '#imagePoint'
                ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                    console.log('-------------------------------------------------------------------')
                    console.log(obj);
                    console.log($("#yearPoint").val())
                    console.log($("select[name='quiz2']").val())
                    this.data={
                        type:'1',
                        year:$("#yearPoint").val(),
                        pid:  $("select[name='quiz2']").val(),
                        imgType:$("select[name='imgtype']").val()
                    }
                    //quiz2
                    //layer.load(); //上传loading
                }
                , progress: function (n, elem) {
                    console.log(elem)
                    var percent = n + '%' //获取进度百分比
                    console.log(percent)
                    element.progress('imageplan2', percent); //可配合 layui 进度条元素使用
                }
                , done: function (res) {
                    // layer.msg('上传成功');
                    layer.msg('上传成功',{time: 1800},function () {
                        element.progress('imageplan2', '0%'); //可配合 layui 进度条元素使用
                    });
                    // element.progress('imageplan', '0%'); //可配合 layui 进度条元素使用
                    console.log(res)
                }
            });
            var demoListView = $('#demoList')
                ,uploadListIns = upload.render({
                elem: '#testList'
                ,url: 'https://httpbin.org/post' //改成您自己的上传接口
                ,accept: 'file'
                ,multiple: true
                ,auto: false
                ,bindAction: '#testListAction'
                ,choose: function(obj){
                    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                    //读取本地文件
                    obj.preview(function(index, file, result){
                        console.log(file)
                        console.log(result);
                        var tr = $(['<tr id="upload-'+ index +'">'
                            ,'<td>'+ file.name +'</td>'
                            ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                            ,'<td>等待上传</td>'
                            ,'<td>'
                            ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                            ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                            ,'</td>'
                            ,'</tr>'].join(''));

                        //单个重传
                        tr.find('.demo-reload').on('click', function(){
                            obj.upload(index, file);
                        });

                        //删除
                        tr.find('.demo-delete').on('click', function(){
                            delete files[index]; //删除对应的文件
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });

                        demoListView.append(tr);
                    });
                }
                ,done: function(res, index, upload){
                    if(res.files.file){ //上传成功
                        var tr = demoListView.find('tr#upload-'+ index)
                            ,tds = tr.children();
                        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                        tds.eq(3).html(''); //清空操作
                        return delete this.files[index]; //删除文件队列已经上传成功的文件
                    }
                    this.error(index, upload);
                }
                ,error: function(index, upload){
                    var tr = demoListView.find('tr#upload-'+ index)
                        ,tds = tr.children();
                    tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                    tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                }
            });
        });
    },function (error) {

    },function () {
        
    })
}
