package com.newfly.pojo;

public class Task
{
    private static Integer[] subNums;

    private int id;
    private int taskId;
    private int subTask;


    public static void initSubNum(Integer[] nums) {
        subNums = nums;
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getSubTask() {
        return subTask;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setSubTask(int subTask) {
        this.subTask = subTask;
    }


    // 是否可以变化 任务阶段
    public boolean changeTask(String taskId, String subTaskId) {
        int a = this.taskId;
        int b = this.subTask + 1;
        if (b > subNums[a]) {
            a++;
            b = 1;
        }
        return Integer.parseInt(taskId) == a && Integer.parseInt(subTaskId) == b;
    }

    // 是否可接受任务
    public boolean isAccept(String taskId, String subTaskId) {
        return false;
    }

    // 是否完成任务
    public boolean Completable(String taskId, String subTaskId) {
        return false;
    }


}
