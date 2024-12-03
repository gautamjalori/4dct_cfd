# -*- coding: utf-8 -*-
"""
Created on Tue Jul 30 15:06:52 2024

@author: jalori
"""

import matplotlib.pyplot as plt 
import csv 

work_dir='D:/Jalori/RobinSequence/CFD/reference_cp/correction_index/from_klone/plot_data/'
mass_flow_outlet='D:/Jalori/RobinSequence/CFD/reference_cp/correction_index/from_klone/plot_data/mass_flow_outlet.csv'

x = [] 
y = [] 

with open(mass_flow_outlet,'r') as csvfile: 
	plots = csv.reader(csvfile, delimiter = ',') 
	
	for row in plots: 
		x.append(row[0]) 
		y.append(row[1]) 
        
print('x')

plt.plot(x, y) 
plt.xlabel('Names') 
plt.ylabel('Ages') 
plt.title('Ages of different persons') 
plt.legend() 
plt.show() 

