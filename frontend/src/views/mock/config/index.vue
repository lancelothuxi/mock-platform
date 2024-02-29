<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名" prop="applicationName">
        <el-input
          v-model="queryParams.applicationName"
          placeholder="请输入应用名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务名" prop="interfaceName">
        <el-input
          v-model="queryParams.interfaceName"
          placeholder="请输入服务名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方法名" prop="methodName">
        <el-input
          v-model="queryParams.methodName"
          placeholder="请输入方法名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组名" prop="groupName">
        <el-input
          v-model="queryParams.groupName"
          placeholder="请输入分组名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['mock:config:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mock:config:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mock:config:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mock:config:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="应用名" align="center" prop="applicationName" />
      <el-table-column label="服务名" align="center" prop="interfaceName" width="500"/>
      <el-table-column label="方法名" align="center" prop="methodName" />
      <el-table-column label="分组名" align="center" prop="groupName" />
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column label="状态" align="center" key="enabled" >
        <template #default="scope">
          <el-switch
            v-model="scope.row.enabled"
            :active-value=1
            :inactive-value=0
            @change="handleStatusChange($event,scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="查看数据" align="center" :show-overflow-tooltip="true">
        <template #default="scope">
          <router-link :to="'/mock/data?mockConfigId' + scope.row.id" class="link-type">
            <span>查看</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updatedTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mock:config:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mock:config:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改mock配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="configRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用名" prop="applicationName">
          <el-input v-model="form.applicationName" placeholder="请输入应用名" />
        </el-form-item>
        <el-form-item label="服务名" prop="interfaceName">
          <el-input v-model="form.interfaceName" placeholder="请输入服务名" />
        </el-form-item>
        <el-form-item label="方法名" prop="methodName">
          <el-input v-model="form.methodName" placeholder="请输入方法名" />
        </el-form-item>
        <el-form-item label="分组名" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入分组名" />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="form.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option
              v-for="dict in sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="createdTime">
          <el-date-picker clearable
            v-model="form.createdTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updatedTime">
          <el-date-picker clearable
            v-model="form.updatedTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择更新时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Config">
import {listConfig, getConfig, delConfig, addConfig, updateConfig, changeConfigStatus} from "@/api/mock/config";

const { proxy } = getCurrentInstance();
const { sys_yes_no } = proxy.useDict('sys_yes_no');

const configList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    applicationName: null,
    interfaceName: null,
    methodName: null,
    groupName: null,
    version: null,
    enabled: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询mock配置列表 */
function getList() {
  loading.value = true;
  listConfig(queryParams.value).then(response => {
    configList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    applicationName: null,
    interfaceName: null,
    methodName: null,
    groupName: null,
    version: null,
    enabled: null,
    type: null,
    createdTime: null,
    updatedTime: null
  };
  proxy.resetForm("configRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加mock配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getConfig(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改mock配置";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["configRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateConfig(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addConfig(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除mock配置编号为"' + _ids + '"的数据项？').then(function() {
    return delConfig(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 用户状态修改  */
function handleStatusChange(val) {
  let text = val == 1 ? "启用" : "停用";
  proxy.$modal.confirm('确认要' + text  + '吗?').then(function () {
    return changeConfigStatus(row.id, val);
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功");
  }).catch(function () {
  });
};
/** 导出按钮操作 */
function handleExport() {
  proxy.download('mock/config/export', {
    ...queryParams.value
  }, `config_${new Date().getTime()}.xlsx`)
}

getList();
</script>
