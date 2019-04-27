# https://jakevdp.github.io/PythonDataScienceHandbook/04.12-three-dimensional-plotting.html

from mpl_toolkits import mplot3d
import numpy as np
import matplotlib.pyplot as plt

print('Loading 1000x1000 grid')
data = np.loadtxt('/home/tj/Downloads/skiing1000x1000.txt', delimiter=' ')
#data = np.delete()

def f(x, y):
    return data

# numpy.linspace(start, stop, num=50, endpoint=True, retstep=False, dtype=None, axis=0)
print('Creating linear spaces for x and y')
x = np.linspace(start = 0, stop = 999, num = 1000, dtype = np.int)
y = np.linspace(start = 0, stop = 999, num = 1000, dtype = np.int)

print('Creating mesh grid')
X, Y = np.meshgrid(x, y)
Z = f(X, Y)

print('Plotting the 3d surface')
fig = plt.figure()
ax = plt.axes(projection='3d')
ax.contour3D(X, Y, Z, 50, cmap='binary')
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_zlabel('z');