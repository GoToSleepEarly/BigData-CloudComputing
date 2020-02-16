# 一、从暴力破解法而来。
'''
// 暴力匹配（伪码）
int search(String pat, String txt) {
    int M = pat.length;
    int N = txt.length;
    for (int i = 0; i < N - M; i++) {
        int j;
        for (j = 0; j < M; j++) {
            if (pat[j] != txt[i+j])
                break;
        }
        // pat 全都匹配了
        if (j == M) return i;
    }
    // txt 中不存在 pat 子串
    return -1;
}
'''
## 怎么优化？
若str2不匹配，不需要将str2重头开始再次匹配，而是移动到“合适的”长度后来匹配下一位。

所以，可以先从暴力解法开始起手。注意，str1的起始位置只进不退，所以优化的next数组一定是求str2的！

# 二、如何优化
见 https://www.zhihu.com/question/21923021。

重点理解下面这个图：
![图片] (https://pic1.zhimg.com/v2-40b4885aace7b31499da9b90b7c46ed3_r.jpg)

str2内部有重复，就是上文说的最长匹配。当str1和str2不匹配时，我们将str2移动到 ※重复位置的下一位※ ！这样在此匹配来优化！

很直观的可以得到最长匹配的pmt数组，但为了写的方便，我们将pmt数组往右移获得next的数组。

根据名字就知道，next数组就是当当前位置不匹配时，下一个匹配的位置！（pmt的最长匹配，返回的是值相同的位置，而next是值相同位置的下一个位置！）

当然可以先写出pmt，再移动pmt得到next；为了编程方便我们直接得出next，只需要把i和j位置是否匹配填到pmt的下一个index就行；

# 代码
'''
int KMP(char * t, char * p) 
{
	int i = 0; 
	int j = 0;

	while (i < strlen(t) && j < strlen(p))
	{
		if (j == -1 || t[i] == p[j]) 
		{
			i++;
           		j++;
		}
	 	else 
           		j = next[j];
    	}

    if (j == strlen(p))
       return i - j;
    else 
       return -1;
}

void getNext(char * p, int * next)
{
	next[0] = -1;
	int i = 0, j = -1;

	while (i < strlen(p))
	{
		if (j == -1 || p[i] == p[j])
		{
			++i;
			++j;
			next[i] = j;
		}	
		else
			j = next[j];
	}
}


'''

