#!/bin/bash

# 配置数据库连接信息
DB_HOST="localhost"
DB_USER="Tester"
DB_PASS="12345678"
DB_NAME="vCampus"

# 指定SQL文件所在的文件夹路径
SQL_FOLDER="."

# 遍历文件夹中的所有SQL文件并执行
for sql_file in "$SQL_FOLDER"/*.sql; do
    if [ -f "$sql_file" ]; then
        echo "正在执行文件: $sql_file"
        mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$sql_file"
        if [ $? -eq 0 ]; then
            echo "文件执行成功: $sql_file"
        else
            echo "文件执行失败: $sql_file"
        fi
    fi
done