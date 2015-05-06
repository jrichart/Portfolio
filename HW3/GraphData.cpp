//---------------------------------------------------------------------------
// GraphData.CPP
// Member function definitions for class Graph
// Author: Joel Richart
//---------------------------------------------------------------------------
// GraphData class:  
//   -- stores a description of the graph vertex
//   -- overloads the output operator to output the description

//---------------------------------------------------------------------------
#include "GraphData.h"

using namespace std;


GraphData::GraphData()
{
}

GraphData::GraphData(GraphData & source)
{
	vertexDesc = source.vertexDesc;
}

GraphData::GraphData(string description)
{
	vertexDesc = description;
}

GraphData::~GraphData()
{
}

ostream & operator<<(ostream &outStream, const GraphData &data)
{
	return outStream << data.vertexDesc;
}
