# What is Persistent Homology?

Persistent Homology describes topological development of a filtered simplicial complex in one variable. Since simplicial complexes have potencially exponential growth in the number of vertices calculating Persistent Homology might seem cumbersome but thanks to independent works of Edelsbrunner, Letscher and Zomorodian in 2000 we have a fast algorithm for that task.

It can be used to measure the resolution of a given feature in a point cloud or any other abstract finite metrical space. We note that such spaces correspond to a weighted complete graph.

The presented program is an approach to calculating the persistent homology of a finite sample of points using heuristics to on the one hand reduce calculation time and on the one hand bypass problems that are getting out of hand when trying to analyze them theoretically.

# What has the Program to offer?

#### Approaches and Bottlenecks

I'm representing a filtration using a tree structure causing high memory costs scaling with filtration size (up to filtration exponential in worst case). The advantage of this structure is a significant reduction in calculation time compared to my other approach where the filtration is calculated very quickly but without memorizing it. That approach is very good when we do not want to calculate homology, but that's what we are after. Therefore i sticked to the tree approach here.

#### Usage

One workflow enhancing feature is the users variable storage, so he can reference previously defined objects in other method calls. For example loading a euclidean PointSet from a directory into the variable S by typing

```powershell
S <- PointSet euclid "your/data/directory"
```

or get a 1000 point sample  from a smooth surface like the Torus:

```powershell
T <- PointSet mapping Torus 1000
```

Distribute 100 Landmarks over it:

```powershell
L <- LandmarkSet 100 T
```

And finally create a 3-Skeleton of the Čech-Filtration of the LandmarkSet L and assign it to the variable name F by executing

```powershell
F <- Filtration 3 cech L
```

If we want to see what we did, we can run `lo` for "list objects" and get the following result.

```shell
User initialized Objects in Memory = [
	{T - Type: PointSet - Description: PointSet from Torus mapping of size 1000}
	{F - Type: Filtration - Description: 3-Skeleton of Čech(L)}
	{L - Type: Landmarks/PointSet - Description: A  randomly  chosen LandmarkSet of the PointSet T}
]
```

A full overview can be found using `help`.

#### Algorithm

As presented by Edelsbrunner and Harer in [1] we can see that once the filtration is calculated, the task of determining persistent homology of it is given through plain matrix reduction over the field of two elements with some restrictions.

That reduction is implemented such that we don't need to hold the complete matrix all calculation long. Only if we have a column vector that is still needed, i.e. it having a trailing 1, we save it. Otherwise it is a linear combination of the ones we already saved and can successively be reduced to 0 or a vector with a trailing 1.

This is a very important trick we make use of there, because saving the whole matrix at one time, even though it is sparse in general, is already near to impossible for a medium sized filtration.

The reduced matrix gives us persistent homology which is usually visualized as a barcode or a persistence diagram. Here is one of the Torus example from above:

![Torus example](.\Torusexample.png)

Anyone familiar with the homology of the torus will directly notice it in here even though we started with 100 points sampled from its surface.



[1] Edelsbrunner, Herbert ; Harer, John: Computational Topology. Providence USA, American Mathematical Society, 2010
