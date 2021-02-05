#define PY_SSIZE_T_CLEAN
#include <Python.h>

int main()
{
	FILE *fp;
	Py_Initialize();
	fp = fopen("test1.py", "r");
	PyRun_SimpleFile(fp, "test1.py");
	Py_Finalize();
	return 0;
}
