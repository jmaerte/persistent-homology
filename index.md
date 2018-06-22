# What is Persistent Homology?

Persistent Homology describes topological development of a filtered simplicial complex in one variable. Since simplicial complexes have potencially exponential growth in the number of vertices calculating Persistent Homology might seem cumbersome but thanks to independent works of Edelsbrunner, Letscher and Zomorodian in 2000 we have a fast algorithm for that task.

It can be used to measure the resolution of a given feature in a point cloud, i.e. a set of points embedded in an euclidean vector space, or more abstract any other finite metrical space. We note that such spaces correspond to a weighted complete graph.

The presented program is an approach to calculating the Persistent Homology of a finite sample of points using heuristics to reduce calculation time on the one hand and on the other hand bypass problems that are getting out of hand when trying to analyze them theoretically.

Using algebraic theorems about homotopy types of nerves and homology one can show, that if the space sampled from and the sample itself is sufficiently nice in some sense we are guaranteed to have atleast one parameter of the Čech-Filtration (and thus the Vietoris-Filtration) in which we restore the homotopy type of the underlying space. Thus mathematical correctness is given.

# What has the Program to offer?

#### Approaches and Bottlenecks

I am representing a filtration using a tree structure causing high memory costs scaling with filtration size (up to filtration exponential in worst case). The advantage of this structure is a significant reduction in calculation time compared to my other approach where the filtration is calculated very quickly but without memorizing it. That approach is very good when we do not want to calculate homology, but that is exactly what we are after. Therefore I stick to the tree approach here.

#### Launch

You can launch the application by executing `java -jar "path/to/jar"`. If you need extra computing power you can add `-Xmx6g` after the path to higher the maximum available memory of the JVM.

#### Usage

One workflow aiding feature is the users variable storage, so he can reference previously defined objects in other method calls.
For example getting a 1000 point sample from a smooth surface like the Torus:

```powershell
T <- PointSet mapping Torus 1000
```

Evenly distribute (by successively maximizing the minimal distance from each landmark to its preceeding landmarks) 100 landmarks over it:

```powershell
L <- LandmarkSet 100 T --maxmin
```

And finally create a 3-Skeleton of the Čech-Filtration of the LandmarkSet L and assign it to the variable name F by executing

```powershell
F <- Filtration 3 cech L
```

If we want to see a summary of what we did, we can run `lo` for "list objects" and get the following output.

```
User initialized Objects in Memory = [
	{T - Type: PointSet - Description: PointSet from Torus mapping of size 1000}
	{F - Type: Filtration - Description: 3-Skeleton of Čech(L)}
	{L - Type: Landmarks/PointSet - Description: A by maxmin chosen LandmarkSet of the PointSet T}
]
```

A full overview can be found using `help`.

#### Algorithm

As presented by Edelsbrunner and Harer in [1] we can see that once the filtration is calculated, the task of determining persistent homology of it is given through plain matrix reduction over the field of two elements with some restrictions.

That reduction is implemented in such a way that we do not need to hold the complete matrix all calculation long. Only if we have a column vector that is still needed, i.e. it having a trailing 1, we save it. Otherwise it is either a linear combination of the ones we already saved and can be discarded or it can successively be reduced to an independent vector with a trailing 1.

This is a very important trick we make use of there, because saving the whole matrix at one time, even though it is sparse in general, is already near to impossible for a medium sized filtration.

The reduced matrix gives us Persistent Homology which is usually visualized as a barcode or persistence diagram. A barcode plot shows the existence of a homology class to a given point of time in the filtration as a black bar whose length is the persistence of this class. We consider a topological feature to be significant if its corresponding persistence bar has an outstanding length.

Here is one of the Torus example from above created by `plot P -k 1 -l 2` and RStudio:

![Torus example](.\Torusexample.png)

Anyone who encountered the homology of the Torus before will directly notice something familiar here even though we started with 1000 points sampled from its surface and chose 100 evenly spreaded landmarks.

The parameter whose existence one can show using algebraic tools I mentioned at the beginning is somewhere between 4 and 5 as the diagram shows, since thats the interval in which the "living" homology classes are the ones of the homology of the Torus.

#### Todo List

- [ ] A more flexible user input for metrics, point types and mappings onto subspaces of some euclidean space.
- [ ] Improve R Script output for large barcode plots. (RStudio console crashes on too long vector inputs)
- [ ] Persistent user home directory for automatically generated images, plot files and more.

[1] Edelsbrunner, Herbert ; Harer, John: Computational Topology. Providence USA, American Mathematical Society, 2010
