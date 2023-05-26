# File Service💡

一个简单的使用Spring连接Minio服务器的Java应用

# 开始

## 1.安装Minio文件服务器

```
docker run \
	-p 9000:9000 -p 9001:9001 \
	--name minio \
	-d --restart=always \
  	-e "MINIO_ACCESS_KEY=minio" \
  	-e "MINIO_SECRET_KEY=minio" \
  	minio/minio:latest server /data \
	--address ':9000' --console-address ':9001'
```

（-v 数据地址映射 略）

## 2.编译运行程序

git clone该项目

修改application.yml文件,

