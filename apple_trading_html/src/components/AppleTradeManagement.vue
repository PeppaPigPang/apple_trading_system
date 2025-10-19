<template>
    <div>
        <div class="table-title">
            <h2>{{ title }}</h2>
        </div>
        <div class="table-container">
            <!-- 新增按钮 -->
            <el-button type="primary" @click="openAddDialog">新增</el-button>

            <!-- 表格主体 -->
            <el-table 
                :data="tableData" 
                border style="width: 100%; 
                margin-top: 20px;"
                highlight-current-row
                >
                <el-table-column prop="orderId" label="订单 ID" width="180"  align="center"/>
                <el-table-column prop="orderName" label="订单名称" align="center"/>
                <el-table-column prop="quantity" label="订单数量" width="80"  align="center"/>
                <el-table-column prop="unit" label="计量单位" width="80"  align="center"/>
                <el-table-column prop="unitPrice" label="单价" width="80"  align="center"/>
                <el-table-column prop="createTime" label="创建时间" width="150"  align="center"/>
                <el-table-column prop="updateTime" label="更新时间" width="150"  align="center"/>
                <!-- 操作栏 -->
                <el-table-column label="操作" width="180" align="center">
                    <template #default="scope">
                        <el-button type="warning" size="small" @click="openEditDialog(scope.row)">修改</el-button>
                        <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
            />

            <!-- 新增/修改对话框 -->
            <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30rem">
                <el-form :model="form" label-width="80px">
                    <el-form-item label="订单 ID" v-if="isEdit">
                        <el-input v-model="form.orderId" :disabled="true"/>
                    </el-form-item>
                    <el-form-item label="订单名称">
                        <el-input v-model="form.orderName"/>
                    </el-form-item>
                    <el-form-item label="订单数量">
                        <el-input-number v-model="form.quantity" :min="1" :max="150"/>
                    </el-form-item>
                    <el-form-item label="计量单位">
                        <el-select v-model="form.unit" placeholder="请选择">
                            <el-option
                                v-for="item in unitEnum"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                                >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="单价">
                        <el-input v-model="form.unitPrice"/>
                    </el-form-item>
                </el-form>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="dialogVisible = false">取消</el-button>
                        <el-button type="primary" @click="submitForm">确认</el-button>
                    </div>
                </template>
            </el-dialog>
        </div>
    </div>
  
</template>

<script setup>
    import { ref, reactive } from 'vue';
    import { ElMessage, ElMessageBox } from 'element-plus';
    import { getAllOrder, updateOrder, addOrder, deleteOrder } from '/src/api/apple_order';

    // 表头
    const title = "苹果交易管理系统";
    // 表格数据
    const tableData = ref([]);
    // 分页参数
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0); // 总条数（模拟）

    const unitEnum = ref([
        {
            value: '个',
            label: '个',
        },
        {
            value: '市斤',
            label: '市斤',
        },
        {
            value: '公斤',
            label: '公斤',
        },
    ]);
    // 对话框状态与表单数据
    const dialogVisible = ref(false);
    const isEdit = ref(false); // 标记是新增还是修改
    const dialogTitle = isEdit ? "新增订单" : "修改订单";
    const form = reactive({
        orderId: '',
        orderName: '',
        quantity: '',
        unit: '',
        unitPrice: '',
    });

    // 分页查询
    const fetchTableData = async () => {
        // 模拟分页数据，实际中替换为 axios.get 等请求
        const req = {
            pageSize: pageSize.value,
            pageNum: currentPage.value
        };
        try {
            debugger;
            const resp = await getAllOrder(req);
            tableData.value = resp.contents;
            total.value = resp.totalNum;
        } catch (e){
            console.error('获取订单列表失败：', e);
        }
    };

    // 页面加载时初始化数据
    fetchTableData();

    // 分页大小改变
    const handleSizeChange = (val) => {
        pageSize.value = val;
        fetchTableData();
    };

    // 当前页改变
    const handleCurrentChange = (val) => {
        currentPage.value = val;
        fetchTableData();
    };

    // 打开新增对话框
    const openAddDialog = () => {
        isEdit.value = false;
        form.orderId = '';
        form.orderName = '';
        form.quantity = '';
        form.unit = '';
        form.unitPrice = '';
        dialogVisible.value = true;
    };

    // 打开修改对话框（回显数据）
    const openEditDialog = (row) => {
        isEdit.value = true;
        form.orderId = row.orderId;
        form.orderName = row.orderName;
        form.quantity = row.quantity;
        form.unit = row.unit;
        form.unitPrice = row.unitPrice;
        dialogVisible.value = true;
    };

    // 提交表单（新增/修改）
    const submitForm = () => {
        const req = {
            orderId: form.orderId,
            orderName: form.orderName,
            quantity: form.quantity,
            unit: form.unit,
            unitPrice: form.unitPrice
        };
        if (isEdit.value) {
            try {
                updateOrder(req)
                .then((resp) => {
                    if(resp && resp.respCode == "000000"){
                        // 刷新表格数据
                        fetchTableData(); 
                        ElMessage.success('修改成功');
                    }else{
                        ElMessageBox.alert(
                            '更新订单失败：' + resp.respMsg,
                            '失败',
                            {
                                confirmButtonText: '确认',
                                type: 'error'
                            }
                        )
                    }
                })
            } catch (e){
                console.error('更新订单失败', e);
                this.$alert('更新订单失败', '失败', {
                    confirmButtonText: '确定',
                    callback: (action) => {
                    },
                });
            }
        } else {
            try {
                addOrder(req)
                .then((resp) => {
                    if(resp && resp.respCode == "000000"){
                        // 刷新表格数据
                        fetchTableData(); 
                        ElMessage.success('新增成功');
                    }else{
                        ElMessageBox.alert(
                            '新增订单失败：' + resp.respMsg,
                            '失败',
                            {
                                confirmButtonText: '确认',
                                type: 'error'
                            }
                        )
                    }
                })
            } catch (e){
                console.error('新增订单失败', e);
                ElMessageBox.alert(
                    '新增订单失败',
                    '失败',
                    {
                        confirmButtonText: '确认',
                        type: 'error'
                    }
                )
            }
        }
        dialogVisible.value = false;
    };

    // 删除操作
    const handleDelete = (row) => {
        ElMessageBox.confirm(
            '确认删除该条数据吗？',
            '提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        .then(() => {
            // 删除请求
            const req = {
                orderId: row.orderId
            };
            try {
                deleteOrder(req)
                    .then((resp) => {
                    if(resp && resp.respCode == "000000"){
                        // 刷新表格数据
                        fetchTableData(); 
                        ElMessage.success('删除成功');
                    }else{
                        ElMessageBox.alert(
                            '删除订单失败：' + resp.respMsg,
                            '失败',
                            {
                                confirmButtonText: '确认',
                                type: 'error'
                            }
                        )
                    }
                })
            } catch (e){
                console.error('删除订单失败', e);
                this.$alert('删除订单失败', '失败', {
                    confirmButtonText: '确定',
                    callback: (action) => {
                    },
                });
            }
        })
        .catch(() => {
            ElMessage.info('已取消删除');
        });
    };
</script>

<style scoped>
    .table-title{
        text-align: center;
    }

    .table-container {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
    }

    .dialog-footer {
        text-align: right;
    }

</style>