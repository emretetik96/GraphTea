// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.plugins.algorithmanimator.tests;

import graphtea.plugins.algorithmanimator.extension.AlgorithmExtension;

public class TestInducedSubgraphs
        extends graphtea.library.algorithms.subgraphs.TestInducedSubgraphs
        implements AlgorithmExtension {

    public String getName() {
        return "Induced Subgraphs Test";
    }

    public String getDescription() {
        return "Just Induced Subgraphs Test, no input from user";
    }
}
