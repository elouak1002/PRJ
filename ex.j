
.class public ex.ex
.super java/lang/Object

.method public static write(I)V 
    .limit locals 1 
    .limit stack 2 
    getstatic java/lang/System/out Ljava/io/PrintStream; 
    iload 0
    invokevirtual java/io/PrintStream/println(I)V
    return 
.end method

.method public static addT(II)I
.limit locals 2
.limit stack 7
addT_Start:
   iload 0
   ldc 0
   if_icmpne If_else_0
   iload 1
   goto If_end_1
If_else_0:
   iload 0
   ldc 1
   isub
   iload 1
   ldc 1
   iadd
   invokestatic ex/ex/addT(II)I
If_end_1:
   ireturn
.end method

.method public static fib(III)I
.limit locals 3
.limit stack 8
fib_Start:
   iload 0
   ldc 0
   if_icmpgt If_else_2
   iload 2
   goto If_end_3
If_else_2:
   iload 0
   ldc 1
   isub
   iload 1
   iload 2
   iadd
   iload 1
   invokestatic ex/ex/fib(III)I
If_end_3:
   ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 1
.limit stack 5
   ldc 20
   ldc 1
   ldc 0
   invokestatic ex/ex/fib(III)I
   dup
   invokestatic ex/ex/write(I)V
   return
.end method

