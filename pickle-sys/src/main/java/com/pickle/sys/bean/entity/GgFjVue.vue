<el-table-column prop="ywUuid" label="业务UUID"/>
<el-table-column prop="fjMc" label="附件名称"/>
<el-table-column prop="fjLj" label="附件路径"/>
<el-table-column prop="fl" label="分类"/>
<el-table-column prop="wjGs" label="文件格式"/>
<el-table-column prop="wjDx" label="文件大小(kb)"/>
<el-table-column prop="scFs" label="上传方式"/>
<el-table-column prop="ajh" label="案卷号"/>


<el-form-item label="业务UUID：" label-width="40%">
	<el-input v-model="from.ywUuid" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="附件路径：" label-width="40%">
	<el-input v-model="from.fjLj" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="文件格式：" label-width="40%">
	<el-input v-model="from.wjGs" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="上传方式：" label-width="40%">
	<el-input v-model="from.scFs" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>

<el-form-item label="附件名称：" label-width="40%">
	<el-input v-model="from.fjMc" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="分类：" label-width="40%">
	<el-input v-model="from.fl" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="文件大小(kb)：" label-width="40%">
	<el-input v-model="from.wjDx" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>
<el-form-item label="案卷号：" label-width="40%">
	<el-input v-model="from.ajh" autocomplete="off" style="width: 70%"></el-input>
</el-form-item>



