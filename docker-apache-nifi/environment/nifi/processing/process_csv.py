import shutil
import pandas as pd

data = pd.read_csv('/data/slice_violations.csv')

data2 = data.iloc[:, 0:2]

data2.to_csv('/data_formatted/slice_violations.csv', sep='\t')