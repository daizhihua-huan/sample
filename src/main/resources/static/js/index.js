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
                console.log(index)
            }else if(index==1){
                console.log(1);
            }else if(index==2){
                console.log(2)

            }else if(index==3){
                AnQuan();
            }else if(index==4){
                user();
            }else if(index==5){
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

//重点工区的方法
function user(){
    $.axget("/getDicImgType",{pid:'2'},function (res) {
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

    })


}

function getImage(pid) {
    $.axget("/getImageFile",{'pid':pid,'type':'1',year:$("#date").val()},function (res) {
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

    })

}



function manage(){
    $.axget("/getDicForPid",{pid:'2'},function (res) {
        console.log(res);
        $('#quiz1').empty();
        $.each(res.data, function (index, item) {
            //"<option value="+item.value+">"+item.name+"</option>")new Option(item.dataCode, item.id)
            // $('#quiz1').append("<option value="+item.id+">"+item.dataCode+"</option>");// 下拉菜单里添加元素
            $('#quiz1').append(new Option(item.dataCode, item.id));// 下拉菜单里添加元素
        });
        layui.form.render("select");//重新渲染 固定写法
        $.axget("/getDicForPid",{pid:'42'},function (res) {
            $.each(res.data, function (index, item) {
                $('#quiz2').append(new Option(item.title, item.id));// 下拉菜单里添加元素
            });
            layui.form.render("select");//重新渲染 固定写法
            city('42');
        },function (error) {

        })



    },function (error) {

    })

    $.axget("/getDicForPid",{pid:"3"},function (res) {
        console.log(res);
        $('#imgtype').empty();
        $.each(res.data, function (index, item) {
            //"<option value="+item.value+">"+item.name+"</option>")new Option(item.dataCode, item.id)
            // $('#quiz1').append("<option value="+item.id+">"+item.dataCode+"</option>");// 下拉菜单里添加元素
            $('#imgtype').append(new Option(item.title, item.dataCode));// 下拉菜单里添加元素
        });
    },function (error) {

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


    $.axget("/getDataForByType",{type:'区域监测',year:$("#date").val()},function (res) {
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
                text: '监测点数量统计图'
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

    });

    $.axget("/getDataForByTypeNutrient",{type:'区域监测',year:$("#date").val()},function (res) {
        var data = res.data;
        var chart = Highcharts.chart('nutrientContainer', {
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
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white',
                            textShadow: '0px 1px 2px black'
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
                data: data
            }]
        });
    },function (error) {

    })

    $.axget("/getDataForByEnvironmentContainer",{type:'区域监测',year:$("#date").val()},function (res) {
        var data = res.data;
        var chart = Highcharts.chart('environmentContainer', {
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
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white',
                            textShadow: '0px 1px 2px black'
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
                data: data
            }]
        });
    },function (error) {

    })
    $.axget("/getLandTypeContainer",{type:'区域监测',year:$("#date").val()},function (res) {
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
                text: '土地利用监测点数量统计图'
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

    });


    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'26'},function (res) {
        console.log(res);
        var data = res.data;
        Highcharts.chart('environmentalHealthContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: '百分比堆叠柱形图'
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

    })

    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'27'},function (res) {
        var data = res.data;
        var chart = Highcharts.chart('nutrientIndicatorsContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: '养分指标柱形图'
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

    })


    $.axget("/getNameCount",{type:'区域监测',year:$("#date").val(),pid:'28'},function (res) {
        var data = res.data;
        var chart = Highcharts.chart('soundDevelopmentContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: '养分指标柱形图'
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

    })



}

function getregionalism(data) {
    console.log(data);
    $.axget("/getNutrientForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function (res) {
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
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white',
                            textShadow: '0px 1px 2px black'
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
    })
    $.axget("/getEnvironmentForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function (res) {
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
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white',
                            textShadow: '0px 1px 2px black'
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
    })

    $.axget("/getLandTypeForCity",{type:'区域监测',year:$("#date").val(),city:data.recordData.dataCode},function (res) {
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
                text: '土地利用监测点数量统计图'
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

    });

}

function yingXiao(){

}

function shouRu(){

}


function city(pid) {
    $('#quiz2').empty();
    $.axget("/getDicForPid",{pid:pid},function (res) {
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

    })
}
